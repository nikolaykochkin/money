package name.nikolaikochkin.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Cacheable
@Table(name = "users")
public class User extends PanacheEntity {

    @NotBlank
    public String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    public String login;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    public String password;

    @Column(unique = true)
    public Long telegramId;

    @WithSession
    public static Uni<User> findByTelegramId(Long telegramId) {
        return find("telegramId", telegramId).singleResult();
    }
}
