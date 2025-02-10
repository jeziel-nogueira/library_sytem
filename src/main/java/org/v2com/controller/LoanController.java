package org.v2com.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.v2com.dto.LoanDTO;
import org.v2com.service.LoanService;

import java.util.UUID;

@Path("/api/v1/loan")
public class LoanController {

    @Inject
    LoanService loanService;


    @GET
    @Path("/")
    public Response getAllLoans() throws Exception {
        return Response.ok(loanService.getAllLoans()).build();
    }

    @GET
    @Path("/id/{id}")
    public Response getLoanById(@PathParam("id")UUID loanId) throws Exception {
        return Response.ok(loanService.getLoanById(loanId)).build();
    }

    @GET
    @Path("/user/{id}")
    public Response getActiveLoansByUserId(@PathParam("id")UUID userId) throws Exception {
        return Response.ok(loanService.getActiveLoansByUserId(userId)).build();
    }

    @GET
    @Path("/book/{id}")
    public Response getLoanByBookId(@PathParam("id")UUID book) throws Exception {
        return Response.ok(loanService.getActiveLoanByBookId(book)).build();
    }

    @POST
    @Path("/")
    public Response createLoan(@QueryParam("bookId") UUID bookId, @QueryParam("userId") UUID userId) throws Exception {
        return Response.ok(loanService.loanBook(bookId, userId)).build();
    }

    @PUT
    @Path("/{id}")
    public Response endLoan(@PathParam("id")UUID loanId) throws Exception {
        loanService.endLoan(loanId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @PUT
    @Path("/renew/{id}")
    public Response renewBookLoan(@PathParam("id")UUID loanId) throws Exception {
        loanService.renewBookLoan(loanId, 14);
        return Response.status(Response.Status.NO_CONTENT).build();
    }




}
