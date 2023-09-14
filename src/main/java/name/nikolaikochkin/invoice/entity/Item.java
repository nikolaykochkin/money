package name.nikolaikochkin.invoice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
public class Item extends PanacheEntity {
    @JsonBackReference("items")
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    public Invoice invoice;

    @NotBlank
    @Column(nullable = false)
    public String name;

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
