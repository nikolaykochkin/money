package name.nikolaikochkin.money.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import name.nikolaikochkin.money.model.DocumentEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Entity
@NamedEntityGraph(
    name = "invoice-items-entity-graph",
    attributeNodes = {
        @NamedAttributeNode(value = "items")
    }
)
@EntityListeners(InvoiceService.class)
public class Invoice extends DocumentEntity {
    public String externalId;

    @NotNull
    @Column(nullable = false)
    public State state = State.NEW;

    @NotBlank
    @Column(unique = true, nullable = false)
    public String url;

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.JSON)
    public String content;

    public String paymentMethod;

    public Currency currency;

    public BigDecimal totalPrice;

    @ManyToOne(cascade = CascadeType.ALL)
    public Seller seller;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Item> items;

    public String error;

    @JsonIgnore
    public void setErrorState(String error) {
        this.error = error;
        this.state = State.ERROR;
    }

    @JsonIgnore
    public String getUrlOrThrow() {
        if (url != null && !url.trim().isEmpty())
            return url;
        else
            throw new IllegalStateException("Invoice url must not be null or blank");
    }

    public enum State {
        NEW, ERROR, DONE
    }

}