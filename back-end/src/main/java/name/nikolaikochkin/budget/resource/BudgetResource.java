package name.nikolaikochkin.budget.resource;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import name.nikolaikochkin.budget.entity.Budget;

public interface BudgetResource extends PanacheEntityResource<Budget, Long> {
}
