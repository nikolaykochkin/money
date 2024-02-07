package name.nikolaikochkin.money.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import name.nikolaikochkin.money.transaction.Transaction;

@ResourceProperties(hal = true)
public interface TransactionResource extends PanacheEntityResource<Transaction, Long> {
}
