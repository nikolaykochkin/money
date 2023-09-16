package name.nikolaikochkin.transaction.resource;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import name.nikolaikochkin.transaction.entity.UserDefaultAccount;

public interface UserDefaultAccountResource extends PanacheEntityResource<UserDefaultAccount, Long> {
}
