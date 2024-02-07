package name.nikolaikochkin.money.invoice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import name.nikolaikochkin.money.category.Category;
import name.nikolaikochkin.money.model.CatalogEntity;

import java.util.Optional;

@Entity
public class Seller extends CatalogEntity {

    @NotNull
    @Column(unique = true)
    public String externalId;

    public String address;
    public String town;
    public String country;

    @ManyToOne
    public Category category;

    public static Optional<Seller> findByExternalId(String externalId) {
        return find("externalId", externalId).firstResultOptional();
    }
}