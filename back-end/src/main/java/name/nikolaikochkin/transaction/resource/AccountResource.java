package name.nikolaikochkin.transaction.resource;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import name.nikolaikochkin.transaction.entity.Account;

public interface AccountResource extends PanacheEntityResource<Account, Long> {
}
