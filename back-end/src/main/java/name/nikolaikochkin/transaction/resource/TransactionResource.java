package name.nikolaikochkin.transaction.resource;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import name.nikolaikochkin.transaction.entity.Transaction;

public interface TransactionResource extends PanacheEntityResource<Transaction, Long> {
}
