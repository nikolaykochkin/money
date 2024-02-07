package name.nikolaikochkin.money.parser.montenegro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.constraint.Assert;
import io.smallrye.config.ConfigMapping;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import name.nikolaikochkin.money.invoice.Invoice;
import name.nikolaikochkin.money.parser.InvoiceParser;
import name.nikolaikochkin.money.parser.montenegro.model.InvoiceMe;
import name.nikolaikochkin.money.parser.montenegro.model.RequestData;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Currency;


@ApplicationScoped
public class MontenegroInvoiceParser implements InvoiceParser {
    @Inject
    MontenegroInvoiceParserConfig config;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    @RestClient
    MontenegroTaxGovRestClient montenegroRestClient;

    @Override
    public void parseInvoice(Invoice invoice) throws Exception {
        fetchInvoiceContent(invoice);
        processInvoiceContent(invoice);
    }

    @Override
    public boolean applicableForInvoice(Invoice invoice) {
        return invoice.getUrlOrThrow().startsWith(config.url());
    }

    private void fetchInvoiceContent(Invoice invoice) {
        Log.debug("Fetching data for " + invoice);
        RequestData requestData = RequestData.urlToRequestData(invoice.getUrlOrThrow());
        Log.debug("Request data + " + requestData);
        invoice.content = montenegroRestClient.verifyInvoice(requestData);
    }

    private void processInvoiceContent(Invoice invoice) throws JsonProcessingException {
        InvoiceMe invoiceMe = convertContentToInvoiceMe(invoice);
        setInvoiceDetails(invoice, invoiceMe);
    }

    private InvoiceMe convertContentToInvoiceMe(Invoice invoice) throws JsonProcessingException {
        Assert.checkNotNullParam("invoice.content", invoice.content);
        return objectMapper.readValue(invoice.content, InvoiceMe.class);
    }

    private void setInvoiceDetails(Invoice invoice, InvoiceMe invoiceMe) {
        invoiceMe.populateInvoice(invoice);
        invoice.currency = config.currency();
        invoice.state = Invoice.State.DONE;
    }
}