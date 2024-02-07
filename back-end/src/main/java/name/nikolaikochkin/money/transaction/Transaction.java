package name.nikolaikochkin.money.transaction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import name.nikolaikochkin.money.account.Account;
import name.nikolaikochkin.money.account.AccountOperation;
import name.nikolaikochkin.money.account.TransactionType;
import name.nikolaikochkin.money.category.Category;
import name.nikolaikochkin.money.invoice.Invoice;
import name.nikolaikochkin.money.model.DocumentEntity;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Entity
@EntityListeners(TransactionService.class)
public class Transaction extends DocumentEntity {
    @NotNull
    @Column(nullable = false)
    public TransactionType type;

    @NotNull
    @ManyToOne(optional = false)
    public Account account;

    @ManyToOne
    public Category category;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(fetch = FetchType.LAZY)
    public Invoice invoice;

    @NotNull
    @Column(nullable = false)
    public Currency currency;

    @NotNull
    @Positive
    @Column(nullable = false)
    public BigDecimal sum;

    @JsonManagedReference("transaction")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AccountOperation> accountOperations;

    public static Transaction findByInvoice(Invoice invoice) {
        return find("invoice", invoice).firstResult();
    }

    public static Transaction createTransactionFromInvoice(Invoice invoice) {
        Transaction transaction = new Transaction();
        transaction.account = Account.getAccountFromInvoice(invoice);
        transaction.invoice = invoice;
        transaction.timestamp = invoice.timestamp;
        transaction.type = TransactionType.EXPENSE;
        transaction.category = invoice.seller.category;
        transaction.currency = invoice.currency;
        transaction.sum = invoice.totalPrice;
        transaction.user = invoice.user;
        transaction.persist();
        return transaction;
    }
}