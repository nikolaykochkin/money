package name.nikolaikochkin.transaction.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import name.nikolaikochkin.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Entity
public class Movement extends PanacheEntity {
    @Column(nullable = false)
    public Instant timestamp;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Account sourceAccount;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Account destinationAccount;

    @Column(nullable = false)
    public Currency currency;

    @Column(nullable = false)
    @Positive
    public BigDecimal sum;

    @ManyToOne
    @JoinColumn(nullable = false)
    public User user;

    public String comment;

    @CreationTimestamp
    public Instant createdTimestamp;

    @UpdateTimestamp
    public Instant updatedTimestamp;
}
