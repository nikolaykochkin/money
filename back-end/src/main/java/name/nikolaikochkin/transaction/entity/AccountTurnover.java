package name.nikolaikochkin.transaction.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"period", "account_id"}),
        indexes = @Index(columnList = "period, account_id")
)
public class AccountTurnover extends PanacheEntity {
    @Column(nullable = false)
    public Instant period;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Account account;

    @Column(nullable = false)
    public BigDecimal turnover;
}