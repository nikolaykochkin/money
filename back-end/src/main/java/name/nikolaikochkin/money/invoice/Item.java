package name.nikolaikochkin.money.invoice;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import name.nikolaikochkin.money.model.CatalogEntity;

import java.math.BigDecimal;


@Entity
public class Item extends CatalogEntity {
    @JsonBackReference
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Invoice invoice;

    public String externalId;

    public String unit;

    @Positive
    @Column(nullable = false)
    public BigDecimal quantity;

    @Positive
    @Column(nullable = false)
    public BigDecimal unitPrice;

    @Positive
    @Column(nullable = false)
    public BigDecimal price;
}