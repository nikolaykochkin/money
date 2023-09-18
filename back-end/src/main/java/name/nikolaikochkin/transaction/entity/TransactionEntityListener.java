package name.nikolaikochkin.transaction.entity;

import io.quarkus.logging.Log;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import name.nikolaikochkin.transaction.AccountBalanceService;

@ApplicationScoped
public class TransactionEntityListener {
    @Inject
    EventBus eventBus;

    @PostPersist
    public void transactionPostPersist(Transaction transaction) {
        Log.debugf("Transaction Post Persist %s", transaction);
        eventBus.send(AccountBalanceService.class.getName(), transaction);
    }

    @PostUpdate
    public void transactionPostUpdate(Transaction transaction) {
        Log.debugf("Transaction Post Update %s", transaction);
        eventBus.send(AccountBalanceService.class.getName(), transaction);
    }

    @PostRemove
    public void transactionPostRemove(Transaction transaction) {
        Log.debugf("Transaction Post Remove %s", transaction);
        eventBus.send(AccountBalanceService.class.getName(), transaction);
    }
}
