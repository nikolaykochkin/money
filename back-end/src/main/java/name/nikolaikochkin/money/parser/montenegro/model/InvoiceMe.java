package name.nikolaikochkin.money.parser.montenegro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import name.nikolaikochkin.money.invoice.Invoice;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceMe {
    public Long id;
    public String iic;
    public BigDecimal totalPrice;
    public BigDecimal totalPriceWithoutVAT;
    public BigDecimal totalVATAmount;
    public Instant dateTimeCreated;
    public List<PaymentMethodMe> paymentMethod;
    public SellerMe seller;
    public List<ItemMe> items;

    // getters and setters omitted for brevity

    public void populateInvoice(Invoice invoice) {
        invoice.timestamp = dateTimeCreated;
        invoice.externalId = iic;

        invoice.seller = Optional.ofNullable(seller)
                .map(SellerMe::convertToInvoiceSeller)
                .orElse(null);

        if (paymentMethod != null && !paymentMethod.isEmpty()) {
            invoice.paymentMethod = paymentMethod.getFirst().typeCode();
        }

        invoice.items = Optional.ofNullable(items)
                .stream()
                .flatMap(List::stream)
                .map(i -> i.convertToInvoiceItem(invoice))
                .collect(Collectors.toList());

        invoice.totalPrice = totalPrice;
    }
}