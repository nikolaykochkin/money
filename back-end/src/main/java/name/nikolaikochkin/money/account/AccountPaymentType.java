package name.nikolaikochkin.money.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import name.nikolaikochkin.money.model.BaseEntity;

import java.util.Optional;

@Entity
public class AccountPaymentType extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public AccountType accountType;

    @NotNull
    @Column(nullable = false, unique = true)
    public String paymentMethod;

    public static Optional<AccountPaymentType> findByPaymentMethod(String paymentMethod) {
        return find("paymentMethod", paymentMethod).firstResultOptional();
    }
}