package name.nikolaikochkin.transaction;

import io.quarkus.logging.Log;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import name.nikolaikochkin.transaction.entity.*;
import org.hibernate.LockMode;
import org.hibernate.reactive.mutiny.Mutiny;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@ApplicationScoped
public class AccountBalanceService {
    private static final Instant MAX = Instant.ofEpochSecond(32503680000L); // 3000-01-01T00:00:00Z

    public static class AccountBalanceData {
        public Instant start;
        public Account account;
        public List<AccountBalance> balances;
        public List<AccountBalance> currentBalances;
        public List<AccountTurnover> turnovers;
        public Mutiny.Session session;
    }

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @ConsumeEvent
    public Uni<Void> handleTransaction(Transaction transaction) {
        Log.debugf("Start updating account balance for transaction %s", transaction);
        return sessionFactory.withTransaction(session -> updateAccountTurnover(transaction, session)
                        .chain(v -> updateAccountBalance(transaction, session)))
                .onFailure().invoke(Log::error);
    }

    private Uni<Void> updateAccountTurnover(Transaction transaction, Mutiny.Session session) {
        return getOrCreateAccountTurnover(transaction, session)
                .chain(accountTurnover -> setAccountTurnoverSum(accountTurnover, session))
                .chain(session::persist)
                .onFailure().invoke(Log::error);
    }

    private Uni<Void> updateAccountBalance(Transaction transaction, Mutiny.Session session) {
        return getAccountBalanceData(transaction, session)
                .chain(this::getCurrentAccountBalances)
                .chain(this::getMonthlyAccountTurnover)
                .map(this::calculateAccountBalances)
                .chain(data -> session.persistAll(data.balances.toArray()))
                .onFailure().invoke(Log::error);
    }

    private Uni<AccountTurnover> getOrCreateAccountTurnover(Transaction transaction, Mutiny.Session session) {
        var period = YearMonth.from(transaction.timestamp.atOffset(UTC))
                .atDay(1).atStartOfDay().toInstant(UTC);
        return session.createQuery(
                        "FROM AccountTurnover WHERE period = :period AND account = :account",
                        AccountTurnover.class)
                .setParameter("period", period)
                .setParameter("account", transaction.account)
                .setLockMode(LockMode.PESSIMISTIC_WRITE)
                .getSingleResultOrNull()
                .replaceIfNullWith(() -> {
                    var newTurnover = new AccountTurnover();
                    newTurnover.account = transaction.account;
                    newTurnover.period = period;
                    newTurnover.turnover = BigDecimal.ZERO;
                    return newTurnover;
                });
    }

    private Uni<AccountTurnover> setAccountTurnoverSum(AccountTurnover accountTurnover, Mutiny.Session session) {
        var end = YearMonth.from(accountTurnover.period.atOffset(UTC))
                .atEndOfMonth().atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
        return session.createQuery(
                        "SELECT SUM(CASE WHEN tr.type = INCOME THEN tr.sum ELSE -tr.sum END) " +
                                "FROM Transaction tr WHERE tr.timestamp BETWEEN :start AND :end AND tr.account = :account ",
                        BigDecimal.class)
                .setParameter("start", accountTurnover.period)
                .setParameter("end", end)
                .setParameter("account", accountTurnover.account)
                .setLockMode(LockMode.PESSIMISTIC_READ)
                .getSingleResult()
                .map(turnover -> {
                    accountTurnover.turnover = turnover;
                    return accountTurnover;
                });
    }


    private Uni<AccountBalanceData> getAccountBalanceData(Transaction transaction, Mutiny.Session session) {
        var data = new AccountBalanceData();

        data.start = YearMonth.from(transaction.timestamp.atOffset(UTC))
                .atDay(1).atStartOfDay().toInstant(UTC);

        data.account = transaction.account;
        data.balances = createEmptyAccountBalances(transaction);
        data.session = session;

        return Uni.createFrom().item(data);
    }

    private Uni<AccountBalanceData> getCurrentAccountBalances(AccountBalanceData data) {
        return data.session.createQuery(
                        "FROM AccountBalance WHERE period >= :start AND account = :account",
                        AccountBalance.class)
                .setParameter("start", data.start)
                .setParameter("account", data.account)
                .setLockMode(LockMode.PESSIMISTIC_WRITE)
                .getResultList()
                .map(balances -> {
                    data.currentBalances = balances;
                    return data;
                });
    }

    private Uni<AccountBalanceData> getMonthlyAccountTurnover(AccountBalanceData data) {
        return data.session.createQuery("FROM AccountTurnover WHERE period >= :start AND account = :account",
                        AccountTurnover.class)
                .setParameter("start", data.start)
                .setParameter("account", data.account)
                .setLockMode(LockMode.PESSIMISTIC_READ)
                .getResultList()
                .map(accountTurnovers -> {
                    data.turnovers = accountTurnovers;
                    return data;
                });
    }

    private AccountBalanceData calculateAccountBalances(AccountBalanceData data) {
        Map<Instant, AccountBalance> currentBalances = data.currentBalances.stream()
                .collect(Collectors.toMap(accountBalance -> accountBalance.period, Function.identity()));
        Map<Instant, BigDecimal> turnovers = data.turnovers.stream()
                .collect(Collectors.toMap(accountTurnover -> accountTurnover.period, accountTurnover -> accountTurnover.turnover));

        BigDecimal balance = Optional.ofNullable(currentBalances.get(data.start))
                .map(accountBalance -> accountBalance.balance)
                .orElse(BigDecimal.ZERO);

        BigDecimal turnover = turnovers.getOrDefault(data.start, BigDecimal.ZERO);

        List<AccountBalance> result = new ArrayList<>();

        data.balances.sort(Comparator.comparing(accountBalance -> accountBalance.period));
        for (AccountBalance accountBalance : data.balances) {
            var currentBalance = currentBalances.getOrDefault(accountBalance.period, accountBalance);
            currentBalance.balance = balance.add(turnover);
            result.add(currentBalance);
            balance = currentBalance.balance;
            turnover = turnovers.getOrDefault(currentBalance.period, BigDecimal.ZERO);
        }

        data.balances = result;

        return data;
    }

    private List<AccountBalance> createEmptyAccountBalances(Transaction transaction) {
        var start = YearMonth.from(transaction.timestamp.atOffset(UTC));
        var end = YearMonth.now();
        var current = start.plusMonths(1);

        List<AccountBalance> balances = new ArrayList<>();

        while (current.isBefore(end)) {
            var balance = new AccountBalance();

            balance.period = current.atDay(1).atStartOfDay().toInstant(UTC);
            balance.account = transaction.account;
            balance.balance = BigDecimal.ZERO;
            balances.add(balance);

            current = current.plusMonths(1);
        }

        var currentBalance = new AccountBalance();
        currentBalance.period = MAX;
        currentBalance.account = transaction.account;
        currentBalance.balance = BigDecimal.ZERO;

        balances.add(currentBalance);

        return balances;
    }
}
