package name.nikolaikochkin.money.parser.montenegro;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import name.nikolaikochkin.money.parser.montenegro.model.RequestData;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "montenegro-tax-gov")
public interface MontenegroTaxGovRestClient {

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.WILDCARD})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/ic/api/verifyInvoice")
    String verifyInvoice(@BeanParam RequestData data);
}