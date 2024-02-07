package name.nikolaikochkin.money.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;


@MappedSuperclass
public abstract class CatalogEntity extends AuditableEntity {

    @NotBlank
    @Column(nullable = false)
    public String name;

    @Column
    public String groupName;

    @Column(nullable = false, columnDefinition = "boolean default false")
    public boolean deleted = false;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupName='" + groupName + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}