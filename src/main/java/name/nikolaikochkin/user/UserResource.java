package name.nikolaikochkin.user;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;

public interface UserResource extends PanacheEntityResource<User, Long> {
}
