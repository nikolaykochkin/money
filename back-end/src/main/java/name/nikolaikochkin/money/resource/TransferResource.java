package name.nikolaikochkin.money.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import name.nikolaikochkin.money.transfer.Transfer;

public interface TransferResource extends PanacheEntityResource<Transfer, Long> {
}
