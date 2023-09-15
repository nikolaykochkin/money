package name.nikolaikochkin.invoice.parser.montenegro.model;

import java.math.BigDecimal;

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
}
