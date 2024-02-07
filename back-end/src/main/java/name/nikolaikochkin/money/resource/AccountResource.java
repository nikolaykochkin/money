package name.nikolaikochkin.money.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import name.nikolaikochkin.money.account.Account;

@ResourceProperties(hal = true)
public interface AccountResource extends PanacheEntityResource<Account, Long> {
}
