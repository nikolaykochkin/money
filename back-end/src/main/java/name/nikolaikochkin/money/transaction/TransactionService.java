package name.nikolaikochkin.money.transaction;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.transaction.Transactional;
import name.nikolaikochkin.money.account.AccountOperation;

@ApplicationScoped
@Transactional
public class TransactionService {
    @PrePersist
    public void transactionPrePersist(Transaction transaction) {
        Log.debugf("Transaction Pre Persist %s", transaction);
        transaction.accountOperations = AccountOperation.getOrCreateTransactionAccountOperations(transaction);
    }

    @PreUpdate
    public void transactionPreUpdate(Transaction transaction) {
        Log.debugf("Transaction Pre Update %s", transaction);
        transaction.accountOperations = AccountOperation.getOrCreateTransactionAccountOperations(transaction);
    }
}