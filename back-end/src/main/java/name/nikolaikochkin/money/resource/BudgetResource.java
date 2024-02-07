package name.nikolaikochkin.money.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import name.nikolaikochkin.money.budget.Budget;

@ResourceProperties(hal = true)
public interface BudgetResource extends PanacheEntityResource<Budget, Long> {
}
