package name.nikolaikochkin.money.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import name.nikolaikochkin.money.model.AuditableEntity;

@Entity
@Table(name = "users")
public class User extends AuditableEntity {

    @Column(nullable = false)
    @NotBlank(message = "Name should be not blank")
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


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", telegramId=" + telegramId +
                '}';
    }
}