package name.nikolaikochkin.money.budget;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import name.nikolaikochkin.money.category.Category;
import name.nikolaikochkin.money.model.BaseEntity;

import java.math.BigDecimal;

@Entity
public class BudgetItem extends BaseEntity {

    @NotNull
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public Budget budget;

    @NotNull
    @ManyToOne(optional = false)
    public Category category;

    @NotNull
    @Positive
    @Column(nullable = false)
    public BigDecimal min;

    @NotNull
    @Positive
    @Column(nullable = false)
    public BigDecimal max;

}