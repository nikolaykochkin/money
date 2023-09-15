package name.nikolaikochkin.transaction.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Category extends PanacheEntity {
    public String categoryGroup;
    public String categoryName;
    public TransactionType transactionType;
}
