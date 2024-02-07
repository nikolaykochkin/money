package name.nikolaikochkin.money.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import name.nikolaikochkin.money.invoice.Seller;

@ResourceProperties(hal = true)
public interface SellerResource extends PanacheEntityResource<Seller, Long> {
}
