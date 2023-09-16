package name.nikolaikochkin.invoice.parser.montenegro;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import name.nikolaikochkin.invoice.entity.Invoice;
import name.nikolaikochkin.invoice.entity.InvoiceState;
import name.nikolaikochkin.invoice.entity.Item;
import name.nikolaikochkin.invoice.entity.Seller;
import name.nikolaikochkin.invoice.parser.montenegro.model.InvoiceMe;
import name.nikolaikochkin.invoice.parser.montenegro.model.ItemMe;
import name.nikolaikochkin.invoice.parser.montenegro.model.RequestData;
import name.nikolaikochkin.invoice.parser.montenegro.model.SellerMe;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MontenegroParser {

    static final Currency EUR = Currency.getInstance("EUR");

    @Inject
    @RestClient
    MontenegroTaxGovRestClient montenegroTaxGovRestClient;

    @Inject
    ObjectMapper objectMapper;

    @NonBlocking
    public Uni<Invoice> parseInvoice(Invoice invoice) {
        var requestData = RequestData.fromUrl(invoice.url)
                .orElseThrow(() -> new RuntimeException("Montenegro URL is not valid"));

        return montenegroTaxGovRestClient.verifyInvoice(requestData)
                .onFailure().retry().atMost(3)
                .onItem().transform(body -> {
                    invoice.content = body;
                    return invoice;
                })
                .chain(Unchecked.function(this::readFromContent));
    }

    @NonBlocking
    private Uni<Invoice> readFromContent(Invoice invoice) throws Exception {
        var invoiceMe = objectMapper.readValue(invoice.content, InvoiceMe.class);
        fillInvoiceFromInvoiceMe(invoice, invoiceMe);
        return Seller.findByExternalId(invoiceMe.seller.idNum)
                .replaceIfNullWith(sellerMeToSeller(invoiceMe.seller))
                .onItem().transform(seller -> {
                    invoice.seller = seller;
                    return invoice;
                });
    }

    private void fillInvoiceFromInvoiceMe(Invoice invoice, InvoiceMe invoiceMe) {
        invoice.timestamp = invoiceMe.dateTimeCreated;
        invoice.externalId = invoiceMe.iic;
        invoice.items = getInvoiceItems(invoiceMe);
        invoice.items.forEach(invoiceItem -> invoiceItem.invoice = invoice);
        invoice.paymentMethod = getPaymentMethod(invoiceMe);
        invoice.currency = EUR;
        invoice.totalPrice = invoiceMe.totalPrice;
        invoice.state = InvoiceState.DONE;
    }

    @NotNull
    private List<Item> getInvoiceItems(InvoiceMe invoiceMe) {
        return Optional.ofNullable(invoiceMe.items)
                .map(l -> l.stream().map(this::itemMeToItem).toList())
                .orElseGet(Collections::emptyList);
    }

    private String getPaymentMethod(InvoiceMe invoiceMe) {
        return Optional.ofNullable(invoiceMe.paymentMethod)
                .map(l -> l.get(0))
                .map(p -> p.typeCode)
                .orElse(null);
    }

    private Seller sellerMeToSeller(SellerMe sellerMe) {
        Seller seller = new Seller();
        seller.name = sellerMe.name;
        seller.externalId = sellerMe.idNum;
        seller.address = sellerMe.address;
        seller.town = sellerMe.town;
        seller.country = sellerMe.country;
        return seller;
    }

    private Item itemMeToItem(ItemMe itemMe) {
        Item item = new Item();

        item.name = itemMe.name;
        item.externalId = itemMe.code;
        item.price = itemMe.priceAfterVat;
        item.unit = itemMe.unit;
        item.quantity = itemMe.quantity;
        item.unitPrice = itemMe.unitPriceAfterVat;

        return item;
    }
}
