package name.nikolaikochkin.money.transfer;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.transaction.Transactional;
import name.nikolaikochkin.money.account.AccountOperation;

@Transactional
@ApplicationScoped
public class TransferService {
    @PrePersist
    public void transactionPrePersist(Transfer transfer) {
        Log.debugf("Transfer Pre Persist %s", transfer);
        transfer.accountOperations = AccountOperation.getOrCreateTransferAccountOperations(transfer);
    }

    @PreUpdate
    public void transactionPreUpdate(Transfer transfer) {
        Log.debugf("Transfer Pre Update %s", transfer);
        transfer.accountOperations = AccountOperation.getOrCreateTransferAccountOperations(transfer);
    }
}
