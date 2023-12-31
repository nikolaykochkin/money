package name.nikolaikochkin.transaction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"period", "account_id"}),
        indexes = @Index(columnList = "period, account_id")
)
public class AccountBalance extends PanacheEntity {
    @Column(nullable = false)
    public Instant period;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Account account;

    @Column(nullable = false)
    public BigDecimal balance;
}
