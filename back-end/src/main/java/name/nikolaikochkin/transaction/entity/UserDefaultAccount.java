package name.nikolaikochkin.transaction.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import name.nikolaikochkin.user.User;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "type"}))
public class UserDefaultAccount extends PanacheEntity {
    @ManyToOne
    @JoinColumn(nullable = false)
    public User user;

    @Column(nullable = false)
    public AccountType type;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Account account;

    @WithSession
    public static Uni<Account> findDefaultAccountByUserAndAccountType(User user, AccountType type) {
        return find("user = :user and type = :type", Parameters.with("user", user).and("type", type))
                .<UserDefaultAccount>firstResult()
                .map(userDefaultAccount -> userDefaultAccount.account);
    }
}
