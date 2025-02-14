package org.v2com.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.v2com.dto.LoanDTO;
import org.v2com.service.LoanService;

import java.util.UUID;
import java.util.List;

@Path("/api/v1/loan")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanController {

    @Inject
    LoanService loanService;

    @Operation(summary = "View all loans", description = "Retrieves a list of all loans")
    @APIResponse(responseCode = "200", description = "List of loans", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = LoanDTO.class)))
    @APIResponse(responseCode = "204", description = "Not found")
    @APIResponse(responseCode = "403", description = "Access denied")
    @GET
    @RolesAllowed("Subscriber")
    public Response getAllLoans() throws Exception {
        return Response.ok(loanService.getAllLoans()).build();
    }

    @Operation(summary = "Get loan by ID", description = "Fetches a loan using its unique identifier")
    @APIResponse(responseCode = "200", description = "Loan details", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LoanDTO.class)))
    @APIResponse(responseCode = "404", description = "Not found")
    @APIResponse(responseCode = "403", description = "Access denied")
    @GET
    @Path("/id/{id}")
    @RolesAllowed("Subscriber")
    public Response getLoanById(@Parameter(description = "UUID of the loan", required = true) @PathParam("id") UUID loanId) throws Exception {
        return Response.ok(loanService.getLoanById(loanId)).build();
    }

    @Operation(summary = "Get loans by user ID", description = "Fetches all loans associated with a specific user")
    @APIResponse(responseCode = "200", description = "List of loans", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = LoanDTO.class)))
    @APIResponse(responseCode = "403", description = "Access denied")
    @APIResponse(responseCode = "404", description = "Not found")
    @GET
    @Path("/user/{id}")
    @RolesAllowed("Subscriber")
    public Response getLoansByUserId(@Parameter(description = "UUID of the user", required = true) @PathParam("id") UUID userId) throws Exception {
        return Response.ok(loanService.getLoansByUserId(userId)).build();
    }

    @Operation(summary = "Get loan by book ID", description = "Fetches the active loan for a specific book")
    @APIResponse(responseCode = "200", description = "Loan details", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LoanDTO.class)))
    @APIResponse(responseCode = "403", description = "Access denied")
    @APIResponse(responseCode = "404", description = "Not found")
    @GET
    @Path("/book/{id}")
    @RolesAllowed("Subscriber")
    public Response getLoanByBookId(@Parameter(description = "UUID of the book", required = true) @PathParam("id") UUID book) throws Exception {
        return Response.ok(loanService.getActiveLoanByBookId(book)).build();
    }

    @Operation(summary = "Create a new loan", description = "Creates a new loan for a book and user")
    @APIResponse(responseCode = "201", description = "Loan created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LoanDTO.class)))
    @APIResponse(responseCode = "403", description = "Access denied")
    @POST
    @RolesAllowed("Subscriber")
    public Response createLoan(@QueryParam("bookId") UUID bookId, @QueryParam("userId") UUID userId) throws Exception {
        return Response.ok(loanService.loanBook(bookId, userId)).build();
    }

    @Operation(summary = "End a loan", description = "Ends an active loan")
    @APIResponse(responseCode = "204", description = "Loan ended successfully")
    @APIResponse(responseCode = "403", description = "Access denied")
    @APIResponse(responseCode = "404", description = "Not found")
    @PUT
    @Path("/{id}")
    @RolesAllowed("Subscriber")
    public Response endLoan(@Parameter(description = "UUID of the loan", required = true) @PathParam("id") UUID loanId) throws Exception {
        loanService.endLoan(loanId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Operation(summary = "Renew a loan", description = "Extends the loan period for a book")
    @APIResponse(responseCode = "204", description = "Loan renewed successfully")
    @PUT
    @RolesAllowed("Subscriber")
    public Response renewBookLoan(@Valid LoanDTO loanDTO) throws Exception {
        loanService.renewBookLoan(loanDTO);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
