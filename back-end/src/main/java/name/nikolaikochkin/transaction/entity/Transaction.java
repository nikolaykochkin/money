package name.nikolaikochkin.transaction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import name.nikolaikochkin.invoice.entity.Invoice;
import name.nikolaikochkin.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Entity
public class Transaction extends PanacheEntity {
    @Column(nullable = false)
    public Instant timestamp;

    @Column(nullable = false)
    public TransactionType type;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Account account;

    @ManyToOne
    public Category category;

    @JsonBackReference("transactions")
    @ManyToOne
    public Invoice invoice;

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
