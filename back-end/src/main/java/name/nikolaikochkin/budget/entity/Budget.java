package name.nikolaikochkin.budget.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import name.nikolaikochkin.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Date;
import java.util.List;

@Entity
public class Budget extends PanacheEntity {
    public Date date;

    @JsonManagedReference("budgetCategories")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "budget")
    public List<BudgetCategory> budgetCategories;

    public Currency currency;

    public BigDecimal totalMin;

    public BigDecimal totalMax;

    @ManyToOne
    @JoinColumn(nullable = false)
    public User user;

    @CreationTimestamp
    public Instant createdTimestamp;

    @UpdateTimestamp
    public Instant updatedTimestamp;
}
