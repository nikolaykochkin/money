package name.nikolaikochkin.money.invoice;

import io.quarkus.logging.Log;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.transaction.Transactional;
import name.nikolaikochkin.money.parser.ParserService;

import java.util.Optional;

@Transactional
@ApplicationScoped
public class InvoiceService {
    @Inject
    EntityManager entityManager;

    @Inject
    EventBus eventBus;

    @PostPersist
    public void invoicePostPersist(Invoice invoice) {
        Log.debug("Invoice Post Persist " + invoice);
        handleInvoice(invoice);
    }

    @PostUpdate
    public void invoicePostUpdate(Invoice invoice) {
        Log.debug("Invoice Post Update " + invoice);
        handleInvoice(invoice);
    }


    public void handleInvoice(Invoice invoice) {
        Log.debug("Handle Invoice " + invoice);
        if (invoice.state == Invoice.State.NEW) {
            eventBus.send(ParserService.class.getName(), invoice);
        }
    }

    @Blocking
    @ConsumeEvent
    public void saveInvoice(Invoice invoice) {
        Log.debug("Saving " + invoice);
        handleInvoiceSeller(invoice);
        if (invoice.isPersistent()) {
            invoice.persistAndFlush();
        } else {
            entityManager.merge(invoice);
        }
    }

    private void handleInvoiceSeller(Invoice invoice) {
        if (invoice.state == Invoice.State.DONE) {
            if (invoice.seller == null) {
                invoice.setErrorState("Seller must not be null");
            } else {
                invoice.seller = getOrCreateInvoiceSeller(invoice.seller);
            }
        }
    }

    private Seller getOrCreateInvoiceSeller(Seller seller) {
        return Optional.ofNullable(seller.externalId)
                .flatMap(Seller::findByExternalId)
                .orElse(seller);
    }
}