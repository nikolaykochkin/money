package name.nikolaikochkin.money.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.logging.Log;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import name.nikolaikochkin.money.model.BaseEntity;
import name.nikolaikochkin.money.transaction.Transaction;
import name.nikolaikochkin.money.transfer.Transfer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

// TODO Either not null transfer or transaction
@Entity
public class AccountOperation extends BaseEntity {
    @NotNull
    @Column(nullable = false)
    public Instant timestamp;

    @NotNull
    @ManyToOne(optional = false)
    public Account account;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public TransactionType type;

    @JsonBackReference("transaction")
    @ManyToOne(fetch = FetchType.LAZY)
    public Transaction transaction;

    @JsonBackReference("transfer")
    @ManyToOne(fetch = FetchType.LAZY)
    public Transfer transfer;

    @NotNull
    @Column(nullable = false)
    public Currency currency;

    @NotNull
    @Column(nullable = false)
    public BigDecimal amount;

    public void fillByTransaction(Transaction transaction) {
        this.timestamp = transaction.timestamp;
        this.account = transaction.account;
        this.type = transaction.type;
        this.transaction = transaction;
        this.currency = transaction.currency;
        this.amount = type == TransactionType.INCOME ? transaction.amount : transaction.amount.negate();
    }

    public void fillByTransfer(Transfer transfer, TransactionType type) {
        this.timestamp = transfer.timestamp;
        this.account = type == TransactionType.EXPENSE ? transfer.sourceAccount : transfer.destinationAccount;
        this.type = type;
        this.transfer = transfer;
        this.currency = transfer.currency;
        this.amount = type == TransactionType.INCOME ? transfer.amount : transfer.amount.negate();
    }

    public static Optional<AccountOperation> findByTransaction(Transaction transaction) {
        return transaction.isPersistent()
                ? find("transaction", transaction).firstResultOptional()
                : Optional.empty();
    }

    public static Optional<AccountOperation> findByTransferAndType(Transfer transfer, TransactionType type) {
        return transfer.isPersistent()
                ? find("transfer = ?1 and type = ?2", transfer, type).firstResultOptional()
                : Optional.empty();
    }

    public static List<AccountOperation> getOrCreateTransactionAccountOperations(Transaction transaction) {
        Log.debugf("Create or update transaction's account operation %s", transaction);
        AccountOperation accountOperation = findByTransaction(transaction).orElseGet(AccountOperation::new);
        accountOperation.fillByTransaction(transaction);
        return List.of(accountOperation);
    }

    public static List<AccountOperation> getOrCreateTransferAccountOperations(Transfer transfer) {
        Log.debugf("Create or update transfer's account operations %s", transfer);
        AccountOperation expense = findByTransferAndType(transfer, TransactionType.EXPENSE).orElseGet(AccountOperation::new);
        expense.fillByTransfer(transfer, TransactionType.EXPENSE);
        AccountOperation income = findByTransferAndType(transfer, TransactionType.INCOME).orElseGet(AccountOperation::new);
        income.fillByTransfer(transfer, TransactionType.INCOME);
        return List.of(expense, income);
    }
}
