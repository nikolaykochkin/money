package name.nikolaikochkin.money.parser.montenegro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import name.nikolaikochkin.money.invoice.Invoice;
import name.nikolaikochkin.money.invoice.Item;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemMe {
    public Long id;
    public String name;
    public String code;
    public String unit;
    public BigDecimal quantity;
    public BigDecimal unitPriceAfterVat;
    public BigDecimal priceBeforeVat;
    public Integer vatRate;
    public BigDecimal vatAmount;
    public BigDecimal priceAfterVat;


    public Item convertToInvoiceItem(Invoice invoice) {
        Item invoiceItem = new Item();
        invoiceItem.invoice = invoice;
        invoiceItem.name = name;
        invoiceItem.externalId = code;
        invoiceItem.price = priceAfterVat;
        invoiceItem.unit = unit;
        invoiceItem.quantity = quantity;
        invoiceItem.unitPrice = unitPriceAfterVat;
        return invoiceItem;
    }
}