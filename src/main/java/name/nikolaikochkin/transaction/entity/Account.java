package name.nikolaikochkin.transaction.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import name.nikolaikochkin.user.User;

import java.util.Currency;

@Entity
public class Account extends PanacheEntity {
    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public AccountType type;

    @Column(nullable = false)
    public Currency currency;

    @ManyToOne
    @JoinColumn(nullable = false)
    public User owner;
}
