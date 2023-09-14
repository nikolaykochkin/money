package name.nikolaikochkin.invoice.resource;

import io.quarkus.hibernate.reactive.rest.data.panache.RestDataResourceMethodListener;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import name.nikolaikochkin.invoice.InvoiceService;
import name.nikolaikochkin.invoice.entity.Invoice;

@ApplicationScoped
public class InvoiceRestDataResourceMethodListener implements RestDataResourceMethodListener<Invoice> {

    @Inject
    EventBus eventBus;

    @Override
    public void onAfterAdd(Invoice invoice) {
        eventBus.requestAndForget(InvoiceService.class.getName(), invoice);
    }

    @Override
    public void onAfterUpdate(Invoice invoice) {
        eventBus.requestAndForget(InvoiceService.class.getName(), invoice);
    }

}
