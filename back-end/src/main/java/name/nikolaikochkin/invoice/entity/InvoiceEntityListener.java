package name.nikolaikochkin.invoice.entity;

import io.quarkus.logging.Log;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import name.nikolaikochkin.invoice.parser.ParserService;
import name.nikolaikochkin.transaction.TransactionService;

@ApplicationScoped
public class InvoiceEntityListener {
    @Inject
    EventBus eventBus;

    @PostPersist
    public void invoicePostPersist(Invoice invoice) {
        Log.debugf("Invoice Post Persist %s", invoice);
        handleInvoice(invoice);
    }

    @PostUpdate
    public void invoicePostUpdate(Invoice invoice) {
        Log.debugf("Invoice Post Update %s", invoice);
        handleInvoice(invoice);
    }

    private void handleInvoice(Invoice invoice) {
        switch (invoice.state) {
            case NEW -> eventBus.send(ParserService.class.getName(), invoice);
            case DONE -> eventBus.send(TransactionService.class.getName(), invoice);
        }
    }
}
