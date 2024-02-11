package name.nikolaikochkin.money.transfer;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import name.nikolaikochkin.money.account.Account;
import name.nikolaikochkin.money.account.AccountOperation;
import name.nikolaikochkin.money.model.DocumentEntity;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Entity
@EntityListeners(TransferService.class)
public class Transfer extends DocumentEntity {
    @ManyToOne
    @JoinColumn(nullable = false)
    public Account sourceAccount;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Account destinationAccount;

    @NotNull
    @Column(nullable = false)
    public Currency currency;

    @NotNull
    @Positive
    @Column(nullable = false)
    public BigDecimal amount;

    @JsonManagedReference("transfer")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "transfer")
    public List<AccountOperation> accountOperations;
}
