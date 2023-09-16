package name.nikolaikochkin.invoice.resource;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import name.nikolaikochkin.invoice.entity.AccountPaymentType;

public interface AccountPaymentTypeResource extends PanacheEntityResource<AccountPaymentType, Long> {
}
