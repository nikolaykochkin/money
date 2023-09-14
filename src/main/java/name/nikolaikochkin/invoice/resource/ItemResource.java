package name.nikolaikochkin.invoice.resource;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import name.nikolaikochkin.invoice.entity.Item;

public interface ItemResource extends PanacheEntityResource<Item, Long> {
}
