package name.nikolaikochkin.money.budget;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import name.nikolaikochkin.money.model.DocumentEntity;

import java.util.Currency;
import java.util.List;

@Entity
public class Budget extends DocumentEntity {
    @NotBlank
    @Column(nullable = false)
    public String name;

    @NotNull
    @Column(nullable = false)
    public Currency currency;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "budget")
    public List<BudgetItem> budgetItems;
}