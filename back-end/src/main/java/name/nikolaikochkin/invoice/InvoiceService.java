package name.nikolaikochkin.invoice;

import io.quarkus.logging.Log;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import name.nikolaikochkin.invoice.entity.Invoice;
import name.nikolaikochkin.invoice.entity.InvoiceState;
import name.nikolaikochkin.invoice.parser.ParserService;
import name.nikolaikochkin.transaction.TransactionService;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.annotations.LogMessage;

import java.util.Objects;

@ApplicationScoped
@SuppressWarnings("unused")
public class InvoiceService {
    @Inject
    EventBus eventBus;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Inject
    TransactionService transactionService;

    @NonBlocking
    @LogMessage
    public Uni<Invoice> save(Invoice invoice) {
        Log.debugf("Saving %s", invoice);
        return sessionFactory
                .withTransaction(((session, transaction) -> session.contains(invoice) ? invoice.persistAndFlush() : session.merge(invoice)))
                .chain(this::invoiceHandler);
    }

    @NonBlocking
    public Uni<Invoice> parse(Invoice invoice) {
        Log.debugf("Parse %s", invoice);
        invoice.state = InvoiceState.PARSING;
        return save(invoice).invoke(i -> eventBus.send(ParserService.class.getName(), i));
    }

    @NonBlocking
    public Uni<Invoice> parsed(Invoice invoice) {
        Log.debugf("Parsed %s", invoice);
        invoice.state = InvoiceState.TRANSACTION;
        return save(invoice);
    }

    @NonBlocking
    public Uni<Invoice> transaction(Invoice invoice) {
        Log.debugf("Transaction %s", invoice);
        if (Objects.isNull(invoice.seller.category)) {
            handleTransactionError(invoice, new RuntimeException("Seller category is empty"));
        } else {
            try {
                invoice.transactions = transactionService.createTransactionsFromInvoice(invoice);
                invoice.state = InvoiceState.DONE;
            } catch (Throwable e) {
                handleTransactionError(invoice, e);
            }
        }
        return save(invoice);
    }

    private void handleTransactionError(Invoice invoice, Throwable e) {
        Log.errorf("Could not create transactions from %s. Cause: %s", invoice, e.getMessage(), e);
        invoice.state = InvoiceState.ERROR;
        invoice.error = e.getMessage();
    }

    @ConsumeEvent
    public Uni<Void> saveOnEvent(Invoice invoice) {
        return save(invoice).replaceWithVoid();
    }

    public Uni<Invoice> invoiceHandler(Invoice invoice) {
        return switch (invoice.state) {
            case NEW -> parse(invoice);
            case PARSED -> parsed(invoice);
            case TRANSACTION -> transaction(invoice);
            default -> Uni.createFrom().item(invoice);
        };
    }


}
