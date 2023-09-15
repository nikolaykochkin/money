package name.nikolaikochkin.telegram;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.NoResultException;
import name.nikolaikochkin.invoice.InvoiceService;
import name.nikolaikochkin.invoice.entity.Invoice;
import name.nikolaikochkin.user.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.ADMIN;
import static io.quarkus.vertx.VertxContextSupport.subscribeAndAwait;

@Startup
@Singleton
@SuppressWarnings("unused")
public class TelegramBot extends AbilityBot {
    Long creatorId;
    InvoiceService invoiceService;

    // TODO: 9.7.23. dump DBContext to DB
    @Inject
    public TelegramBot(@ConfigProperty(name = "application.bot.token") String botToken,
                       @ConfigProperty(name = "application.bot.username") String botName,
                       @ConfigProperty(name = "application.bot.creatorId") Long creatorId,
                       InvoiceService invoiceService) {
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

    public Ability handleInvoiceUrl() {
        return Ability.builder()
                .name("invoice")
                .info("Handle invoice URL")
                .input(1)
                .locality(USER)
                .privacy(ADMIN)
                .action(this::handleInvoiceUrl)
                .build();
    }

    public void handleInvoiceUrl(MessageContext ctx) {
        try {
            User user = subscribeAndAwait(() -> User.findByTelegramId(ctx.user().getId())
                    .onFailure(NoResultException.class).transform(e -> new IllegalArgumentException("User not found")));

            silent.send("Hello " + user.name, ctx.chatId());

            Invoice invoice = new Invoice();
            invoice.url = ctx.firstArg();
            invoice.user = user;

            Invoice saved = subscribeAndAwait(() -> invoiceService.save(invoice));

            // TODO: 30.7.23. HTML invoice view and sendMd
            String message = "Invoice saved:\n" + JsonObject.mapFrom(saved).encodePrettily();
            silent.send(message, ctx.chatId());
        } catch (Throwable e) {
            Log.errorf("Couldn't handle invoice url. Cause: %s", e.getMessage(), e);
            silent.send("Something went wrong:\n" + e.getMessage(), ctx.chatId());
        }
    }
}
