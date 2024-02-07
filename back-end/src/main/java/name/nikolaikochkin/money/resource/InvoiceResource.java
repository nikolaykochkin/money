package name.nikolaikochkin.money.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import name.nikolaikochkin.money.invoice.Invoice;
import name.nikolaikochkin.money.transaction.Transaction;

import java.net.URI;

@ResourceProperties(hal = true)
public interface InvoiceResource extends PanacheEntityResource<Invoice, Long> {
    @POST
    @Path("/{id}/transaction")
    @Produces("application/json")
    @Transactional
    default Response createTransaction(@PathParam("id") Long id, @Context UriInfo uriInfo) {
        Invoice invoice = Invoice.findById(id);
        if (invoice == null) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(),
                    "Invoice with id " + id + " not found").build();
        }

        if (invoice.state != Invoice.State.DONE) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(),
                    "Invoice must be in status DONE").build();
        }

        Transaction transaction = Transaction.findByInvoice(invoice);
        if (transaction != null) {
            URI location = uriInfo.getBaseUriBuilder().path("transaction").path(transaction.id.toString()).build();
            return Response.status(Response.Status.CONFLICT.getStatusCode(),
                            "Transaction for this invoice already exists")
                    .location(location)
                    .entity(transaction)
                    .build();
        }

        transaction = Transaction.createTransactionFromInvoice(invoice);
        URI location = uriInfo.getBaseUriBuilder().path("transaction").path(transaction.id.toString()).build();
        return Response.created(location).entity(transaction).build();
    }
}
