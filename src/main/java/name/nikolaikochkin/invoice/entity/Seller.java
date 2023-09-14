package name.nikolaikochkin.invoice.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import name.nikolaikochkin.transaction.entity.Category;

@Entity
public class Seller extends PanacheEntity {
    @NotBlank
    @Column(nullable = false)
    public String name;

    @Column(unique = true)
    public String externalId;

    public String address;

    public String town;

    public String country;

    @ManyToOne
    public Category category;

    @WithSession
    public static Uni<Seller> findByExternalId(String externalId) {
        return Seller.find("externalId", externalId).firstResult();
    }
}
