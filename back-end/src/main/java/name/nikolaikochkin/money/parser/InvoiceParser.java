package name.nikolaikochkin.money.parser;

import name.nikolaikochkin.money.invoice.Invoice;

public interface InvoiceParser {
    void parseInvoice(Invoice invoice) throws Exception;
    boolean applicableForInvoice(Invoice invoice);
}