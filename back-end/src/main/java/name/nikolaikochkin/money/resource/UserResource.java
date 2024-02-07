package name.nikolaikochkin.money.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import name.nikolaikochkin.money.user.User;

@ResourceProperties(hal = true)
public interface UserResource extends PanacheEntityResource<User, Long> {
}
