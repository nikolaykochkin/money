package name.nikolaikochkin.transaction;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import name.nikolaikochkin.invoice.entity.Invoice;
import name.nikolaikochkin.transaction.entity.*;
import name.nikolaikochkin.user.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TransactionService {
    static final Map<String, AccountType> PAYMENT_ACCOUNT_TYPES = new HashMap<>();

    static {
        PAYMENT_ACCOUNT_TYPES.put("BANKNOTE", AccountType.CASH);
        PAYMENT_ACCOUNT_TYPES.put("CARD", AccountType.CARD);
    }

    @WithTransaction
    @NonBlocking
    public Uni<Transaction> save(Transaction transaction) {
        return transaction.persistAndFlush();
    }


    @NonBlocking
    public List<Transaction> createTransactionsFromInvoice(Invoice invoice) {
        var transaction = new Transaction();

        transaction.timestamp = invoice.timestamp;
        transaction.type = TransactionType.EXPENSE;
        transaction.account = getUserDefaultAccount(invoice.user);
        transaction.category = invoice.seller.category;
        transaction.invoice = invoice;
        transaction.currency = invoice.currency;
        transaction.sum = invoice.totalPrice;
        transaction.user = invoice.user;

        return Collections.singletonList(transaction);
    }

    private Account getUserDefaultAccount(User user) {
        var account = new Account();
        account.id = 1L;
        return account;
    }
}
