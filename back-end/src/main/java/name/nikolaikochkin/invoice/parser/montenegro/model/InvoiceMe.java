package name.nikolaikochkin.invoice.parser.montenegro.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

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
}
