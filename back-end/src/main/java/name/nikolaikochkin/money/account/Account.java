package name.nikolaikochkin.money.account;

import io.smallrye.common.constraint.Assert;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import name.nikolaikochkin.money.invoice.Invoice;
import name.nikolaikochkin.money.model.CatalogEntity;
import name.nikolaikochkin.money.user.User;

import java.util.Currency;

@Entity
public class Account extends CatalogEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public AccountType type;

    @NotNull
    @Column(nullable = false)
    public Currency currency;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public User owner;

    public static Account getAccountFromInvoice(Invoice invoice) {
        var paymentMethod = Assert.checkNotNullParam("invoice.paymentMethod", invoice.paymentMethod);
        var user = Assert.checkNotNullParam("invoice.user", invoice.user);

        AccountPaymentType accountPaymentType = AccountPaymentType.findByPaymentMethod(paymentMethod)
                .orElseThrow(() -> new IllegalArgumentException("Account payment type for payment method '"
                        + paymentMethod + "' not found"));
        return UserDefaultAccount
                .findByUserAndType(user, accountPaymentType.accountType)
                .map(udf -> udf.account)
                .orElseThrow(() -> new IllegalArgumentException("Default account for user '" + user + "' and type '"
                        + accountPaymentType.accountType + "' not found"));
    }
}