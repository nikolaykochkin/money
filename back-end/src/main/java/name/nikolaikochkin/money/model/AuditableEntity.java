package name.nikolaikochkin.money.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@MappedSuperclass
public abstract class AuditableEntity extends BaseEntity {
    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Instant createdTimestamp;

    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Instant updatedTimestamp;
}