package name.nikolaikochkin.invoice.parser.montenegro;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import name.nikolaikochkin.invoice.parser.montenegro.model.RequestData;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://mapr.tax.gov.me")
public interface MontenegroTaxGovRestClient {
    @POST
    @Produces(value = {
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN,
            MediaType.WILDCARD
    })
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ClientHeaderParam(name = "Accept-Encoding", value = "gzip, deflate, br")
    @ClientHeaderParam(name = "Connection", value = "keep-alive")
    @Path("/ic/api/verifyInvoice")
    Uni<String> verifyInvoice(@BeanParam RequestData data);
}
