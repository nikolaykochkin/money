package name.nikolaikochkin.invoice.parser;

import io.quarkus.logging.Log;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import name.nikolaikochkin.invoice.entity.InvoiceEntityListener;
import name.nikolaikochkin.invoice.entity.*;
import name.nikolaikochkin.invoice.parser.montenegro.MontenegroParser;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.Objects;

import static io.quarkus.vertx.VertxContextSupport.subscribeAndAwait;
import static io.smallrye.common.constraint.Assert.assertNotNull;

@ApplicationScoped
@SuppressWarnings("unused")
public class ParserService {
    // TODO: 9.8.23. to properties
    static final String montenegroBaseUrl = "https://mapr.tax.gov.me";

    @Inject
    EventBus eventBus;

    @Inject
    MontenegroParser montenegroParser;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @NonBlocking
    @ConsumeEvent
    public Uni<Void> parseInvoice(Invoice invoice) {
        Log.debugf("Start parsing %s", invoice);

        assertNotNull(invoice);
        assertNotNull(invoice.url);

        Uni<Invoice> invoiceUni;

        if (invoice.url.startsWith(montenegroBaseUrl)) {
            invoiceUni = montenegroParser.parseInvoice(invoice);
        } else {
            invoiceUni = Uni.createFrom().failure(() -> new RuntimeException("Unknown URL domain"));
        }

        return invoiceUni
                .onItem().call(this::paymentMethodToAccountType)
                .onFailure().recoverWithItem(throwable -> handleError(invoice, throwable))
                .chain(i -> sessionFactory.withTransaction(session -> session.merge(i)))
                .replaceWithVoid();
    }

    private Uni<Invoice> paymentMethodToAccountType(Invoice invoice) {
        return AccountPaymentType.findAccountTypeByPaymentMethod(invoice.paymentMethod)
                .onItem().transform(accountType -> {
                    invoice.accountType = accountType;
                    return invoice;
                });
    }

    private Invoice handleError(Invoice invoice, Throwable e) {
        Log.errorf("Could not parse %s. Cause: %s", invoice, e.getMessage(), e);
        invoice.state = InvoiceState.ERROR;
        invoice.error = e.getMessage();
        return invoice;
    }
}
