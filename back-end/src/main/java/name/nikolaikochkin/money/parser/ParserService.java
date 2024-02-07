package name.nikolaikochkin.money.parser;

import io.quarkus.arc.All;
import io.quarkus.logging.Log;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import name.nikolaikochkin.money.invoice.Invoice;
import name.nikolaikochkin.money.invoice.InvoiceService;

import java.util.List;

@ApplicationScoped
public class ParserService {

    @Inject
    EventBus eventBus;

    @All
    @Inject
    List<InvoiceParser> parsers;

    @Blocking
    @ConsumeEvent
    public void parseInvoice(Invoice invoice) {
        Log.debugf("Parsing %s", invoice);
        try {
            InvoiceParser parser = findApplicableParser(invoice);
            parser.parseInvoice(invoice);
        } catch (Exception e) {
            Log.errorf("Couldn't parse invoice %s cause %s", invoice, e.getMessage(), e);
            invoice.setErrorState(e.getMessage());
        }
        eventBus.send(InvoiceService.class.getName(), invoice);
    }

    private InvoiceParser findApplicableParser(Invoice invoice) {
        return parsers.stream()
                .filter(p -> p.applicableForInvoice(invoice))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Parser not found"));
    }
}