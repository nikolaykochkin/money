package name.nikolaikochkin.money.parser.montenegro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import name.nikolaikochkin.money.invoice.Seller;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerMe {
    public String idType;
    public String idNum;
    public String name;
    public String address;
    public String town;
    public String country;

    public Seller convertToInvoiceSeller() {
        Seller seller = new Seller();

        seller.name = name;
        seller.externalId = idNum;
        seller.address = address;
        seller.town = town;
        seller.country = country;

        return seller;
    }
}