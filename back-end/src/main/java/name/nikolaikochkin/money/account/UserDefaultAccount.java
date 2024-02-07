package name.nikolaikochkin.money.account;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import name.nikolaikochkin.money.model.BaseEntity;
import name.nikolaikochkin.money.user.User;

import java.util.Optional;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "type"}))
public class UserDefaultAccount extends BaseEntity {
    @NotNull
    @ManyToOne(optional = false)
    public User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public AccountType type;

    @NotNull
    @ManyToOne(optional = false)
    public Account account;

    public static Optional<UserDefaultAccount> findByUserAndType(User user, AccountType type) {
        return find("user = ?1 and type = ?2", user, type).firstResultOptional();
    }
}