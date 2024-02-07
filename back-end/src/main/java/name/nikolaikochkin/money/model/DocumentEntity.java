package name.nikolaikochkin.money.model;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import name.nikolaikochkin.money.user.User;

import java.time.Instant;


@MappedSuperclass
public abstract class DocumentEntity extends AuditableEntity {
    @Column(nullable = false)
    public Instant timestamp = Instant.now();

    @Column(nullable = false, columnDefinition = "boolean default false")
    public boolean deleted = false;

    @NotNull
    @ManyToOne(optional = false)
    public User user;

    public String comment;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", deleted=" + deleted +
                ", user=" + user +
                '}';
    }
}