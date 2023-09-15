package name.nikolaikochkin.invoice.resource;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import name.nikolaikochkin.invoice.entity.Seller;

public interface SellerResource extends PanacheEntityResource<Seller, Long> {
}
