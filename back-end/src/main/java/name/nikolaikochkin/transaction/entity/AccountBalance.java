package name.nikolaikochkin.transaction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Entity
public class AccountBalance extends PanacheEntity {
    @Column(nullable = false)
    public Instant period;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Account account;

    @Column(nullable = false)
    public Currency currency;

    @Column(nullable = false)
    public BigDecimal balance;
}
