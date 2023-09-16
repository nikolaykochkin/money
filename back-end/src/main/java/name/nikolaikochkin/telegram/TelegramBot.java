package name.nikolaikochkin.telegram;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.NoResultException;
import name.nikolaikochkin.invoice.entity.InvoiceEntityListener;
import name.nikolaikochkin.invoice.entity.Invoice;
import name.nikolaikochkin.user.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.function.Predicate;

import static io.quarkus.vertx.VertxContextSupport.subscribeAndAwait;
import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.ADMIN;

@Startup
@Singleton
@SuppressWarnings("unused")
public class TelegramBot extends AbilityBot {
    Long creatorId;
    InvoiceEntityListener invoiceService;

    // TODO: 9.7.23. dump DBContext to DB
    @Inject
    public TelegramBot(@ConfigProperty(name = "application.bot.token") String botToken,
                       @ConfigProperty(name = "application.bot.username") String botName,
                       @ConfigProperty(name = "application.bot.creatorId") Long creatorId,
                       InvoiceEntityListener invoiceService) {
        super(botToken, botName);
        this.creatorId = creatorId;
        this.invoiceService = invoiceService;
    }

    @PostConstruct
    public void registerBot() throws TelegramApiException {
        new TelegramBotsApi(DefaultBotSession.class).registerBot(this);
    }

    @Override
    public long creatorId() {
        return creatorId;
    }

    public Ability invoice() {
        String message = "Send me invoice URL";
        return Ability.builder()
                .name("invoice")
                .info("Handle invoice URL")
                .input(0)
                .locality(USER)
                .privacy(ADMIN)
                .action(ctx -> silent.forceReply(message, ctx.chatId()))
                .reply(this::handleInvoiceUrl, Flag.REPLY, Flag.TEXT, isReplyToBot(), isReplyToMessage(message))
                .build();
    }

    private Predicate<Update> isReplyToMessage(String message) {
        return upd -> {
            Message reply = upd.getMessage().getReplyToMessage();
            return reply.hasText() && reply.getText().equals(message);
        };
    }

    private Predicate<Update> isReplyToBot() {
        return upd -> upd.getMessage().getReplyToMessage().getFrom().getIsBot();
    }

    public void handleInvoiceUrl(BaseAbilityBot bot, Update update) {
        Log.debug(update);
        long userId = update.getMessage().getFrom().getId();
        try {
            User user = subscribeAndAwait(() -> User.findByTelegramId(userId)
                    .onFailure(NoResultException.class).transform(e -> new IllegalArgumentException("User not found")));

            silent.send("Hello " + user.name, userId);

            Invoice invoice = new Invoice();
            invoice.url = update.getMessage().getText();
            invoice.user = user;

            Invoice saved = subscribeAndAwait(() -> addInvoice(invoice));

            // TODO: 30.7.23. HTML invoice view and sendMd
            String message = "Invoice saved:\n" + JsonObject.mapFrom(saved).encodePrettily();
            silent.send(message, userId);
        } catch (Throwable e) {
            Log.errorf("Couldn't handle invoice url. Cause: %s", e.getMessage(), e);
            silent.send("Something went wrong:\n" + e.getMessage(), userId);
        }
    }

    @WithTransaction
    Uni<Invoice> addInvoice(Invoice invoice) {
        return invoice.persistAndFlush();
    }
}
