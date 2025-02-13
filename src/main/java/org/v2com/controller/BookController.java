package org.v2com.controller;


import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.v2com.dto.BookDTO;
import org.v2com.service.BookService;

import java.util.UUID;

@Path("/api/v1/book")
@RequestScoped
public class BookController {
    @Inject
    BookService bookService;

    @GET
    @Path("")
    @RolesAllowed("Subscriber")
    public Response getAllBooks() throws Exception {
        return Response.ok(bookService.findAllBooks()).build();
    }

    @GET
    @Path("/id/{id}")
    public Response getBookById(@PathParam("id")UUID bookId) throws Exception {
        return Response.ok(bookService.findBookById(bookId)).build();
    }

    @GET
    @Path("/search")
    public Response searchBookByArgs(
            @QueryParam("title") String title,
            @QueryParam("author") String author,
            @QueryParam("tag") String tag) throws Exception {

        return Response.ok(bookService.searchBooksByArgs(title, author, tag)).build();
    }

    @POST
    @Path("/register")
    public Response registerNewBook(@Valid BookDTO bookDTO) throws Exception {
        bookDTO = bookService.persistBook(bookDTO);
        return Response.ok(bookDTO).build();
    }

    @PUT
    @Path("/update")
    public Response updateBook(@Valid BookDTO bookDTO) throws Exception {
        bookService.updateBook(bookDTO);
        return Response.ok().build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteBook(@PathParam("id") UUID bookId) throws Exception {
        bookService.deleteBook(bookId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
