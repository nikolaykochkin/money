package name.nikolaikochkin.invoice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import name.nikolaikochkin.transaction.entity.AccountType;
import name.nikolaikochkin.transaction.entity.Transaction;
import name.nikolaikochkin.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;

@Entity
@EntityListeners(InvoiceEntityListener.class)
public class Invoice extends PanacheEntity {

    public Instant timestamp = Instant.now();

    public String externalId;

    public InvoiceState state = InvoiceState.NEW;

    @NotBlank
    @URL(protocol = "https")
    @Column(unique = true, nullable = false)
    public String url;

    @JdbcTypeCode(SqlTypes.JSON)
    public String content;

    public String paymentMethod;

    public AccountType accountType;

    public Currency currency;

    public BigDecimal totalPrice;

    // TODO: 30.7.23. CascadeType without delete
    @ManyToOne(cascade = CascadeType.ALL)
    public Seller seller;

    @JsonManagedReference("items")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "invoice")
    public List<Item> items;

    @JsonManagedReference("transactions")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "invoice")
    public List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(nullable = false)
    public User user;

    public String error;

    @CreationTimestamp
    public Instant createdTimestamp;

    @UpdateTimestamp
    public Instant updatedTimestamp;

    @WithSession
    public static Uni<Invoice> findByUrl(String url) {
        return Invoice.find("url", url).firstResult();
    }
}
