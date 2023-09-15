package name.nikolaikochkin.invoice.resource;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.MethodProperties;
import io.quarkus.rest.data.panache.ResourceProperties;
import io.smallrye.mutiny.Uni;
import name.nikolaikochkin.invoice.entity.Invoice;

public interface InvoiceResource extends PanacheEntityResource<Invoice, Long> {
}
