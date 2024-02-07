package name.nikolaikochkin.money.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import name.nikolaikochkin.money.account.AccountPaymentType;

@ResourceProperties(hal = true)
public interface AccountPaymentTypeResource extends PanacheEntityResource<AccountPaymentType, Long> {
}
