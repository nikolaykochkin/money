package name.nikolaikochkin.budget.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import name.nikolaikochkin.transaction.entity.Category;

import java.math.BigDecimal;

@Entity
public class BudgetCategory extends PanacheEntity {
    @JsonBackReference("budgetCategories")
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    public Budget budget;

    @ManyToOne
    public Category category;

    public BigDecimal min;

    public BigDecimal max;
}
