package name.nikolaikochkin.invoice.parser;

import io.quarkus.logging.Log;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import name.nikolaikochkin.invoice.InvoiceService;
import name.nikolaikochkin.invoice.entity.Invoice;
import name.nikolaikochkin.invoice.entity.InvoiceState;
import name.nikolaikochkin.invoice.entity.Seller;
import name.nikolaikochkin.invoice.parser.montenegro.MontenegroParser;

import java.time.Duration;
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

    @Blocking
    @ConsumeEvent
    public void parseInvoice(Invoice invoice) {
        Log.debugf("Start parsing %s", invoice);

        assertNotNull(invoice);
        assertNotNull(invoice.url);

        if (invoice.url.startsWith(montenegroBaseUrl)) {
            try {
                montenegroParser.parseInvoice(invoice);
            } catch (Throwable e) {
                handleError(invoice, e);
            }
        } else {
            handleError(invoice, new RuntimeException("Unknown URL domain"));
        }

        eventBus.requestAndForget(InvoiceService.class.getName(), invoice);
    }

    private void handleError(Invoice invoice, Throwable e) {
        Log.errorf("Could not parse %s. Cause: %s", invoice, e.getMessage(), e);
        invoice.state = InvoiceState.ERROR;
        invoice.error = e.getMessage();
    }
}
