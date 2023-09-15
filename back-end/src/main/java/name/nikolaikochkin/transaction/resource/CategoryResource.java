package name.nikolaikochkin.transaction.resource;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import name.nikolaikochkin.transaction.entity.Category;

public interface CategoryResource extends PanacheEntityResource<Category, Long> {
}
