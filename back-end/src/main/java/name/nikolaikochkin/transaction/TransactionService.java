package name.nikolaikochkin.transaction;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import name.nikolaikochkin.invoice.entity.Invoice;
import name.nikolaikochkin.transaction.entity.Account;
import name.nikolaikochkin.transaction.entity.Transaction;
import name.nikolaikochkin.transaction.entity.TransactionType;
import name.nikolaikochkin.transaction.entity.UserDefaultAccount;

import java.util.Objects;

@ApplicationScoped
@SuppressWarnings("unused")
public class TransactionService {
    @Inject
    EventBus eventBus;

    public Transaction createTransactionsFromInvoice(Invoice invoice, Account account) {
        var transaction = new Transaction();

        transaction.timestamp = invoice.timestamp;
        transaction.type = TransactionType.EXPENSE;
        transaction.account = account;
        transaction.category = invoice.seller.category;
        transaction.invoice = invoice;
        transaction.currency = invoice.currency;
        transaction.sum = invoice.totalPrice;
        transaction.user = invoice.user;

        return transaction;
    }

    @ConsumeEvent
    @WithTransaction
    public Uni<Void> handleInvoice(Invoice invoice) {
        if (Objects.isNull(invoice.accountType)) {
            return Uni.createFrom().voidItem();
        }
        return UserDefaultAccount.findDefaultAccountByUserAndAccountType(invoice.user, invoice.accountType)
                .onItem().transform(account -> createTransactionsFromInvoice(invoice, account))
                .chain(Transaction::persistAndFlush)
                .replaceWithVoid();
    }
}
