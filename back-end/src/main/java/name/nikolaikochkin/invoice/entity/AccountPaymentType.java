package name.nikolaikochkin.invoice.entity;

import com.google.common.base.Strings;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.common.WithSession;

import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import name.nikolaikochkin.transaction.entity.AccountType;

import java.util.Objects;

@Entity
public class AccountPaymentType extends PanacheEntity {
    @Column(nullable = false)
    public AccountType accountType;
    @Column(nullable = false, unique = true)
    public String paymentMethod;

    @WithSession
    public static Uni<AccountType> findAccountTypeByPaymentMethod(String paymentMethod) {
        if (Objects.isNull(paymentMethod)) {
            return Uni.createFrom().nullItem();
        }
        return find("paymentMethod", paymentMethod)
                .<AccountPaymentType>firstResult()
                .map(accountPaymentType -> accountPaymentType.accountType);
    }
}
