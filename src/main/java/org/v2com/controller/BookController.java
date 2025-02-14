package org.v2com.controller;


import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.v2com.dto.BookDTO;
import org.v2com.service.BookService;

import java.util.UUID;
import java.util.List;

@Path("/api/v1/book")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {
    @Inject
    BookService bookService;

    @Inject
    JsonWebToken jwt;

    @Operation(summary = "View all books", description = "Retrieves a list of all books in the library")
    @APIResponse(responseCode = "200", description = "List of books", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = BookDTO.class)))
    @APIResponse(responseCode = "204", description = "Not found")
    @GET
    public Response getAllBooks() throws Exception {
        List<BookDTO> books = bookService.findAllBooks();
        return Response.ok(books).build();
    }

    @Operation(summary = "Get book by ID", description = "Fetches a book using its unique identifier")
    @APIResponse(responseCode = "200", description = "Book details", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BookDTO.class)))
    @APIResponse(responseCode = "404", description = "Not found")
    @GET
    @Path("/id/{id}")
    public Response getBookById(@Parameter(description = "UUID of the book", required = true) @PathParam("id") UUID bookId) throws Exception {
        return Response.ok(bookService.findBookById(bookId)).build();
    }

    @Operation(summary = "Search books", description = "Searches for books based on title, author, or tag")
    @APIResponse(responseCode = "200", description = "List of books matching search criteria", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = BookDTO.class)))
    @APIResponse(responseCode = "204", description = "Not found")
    @GET
    @Path("/search")
    public Response searchBookByArgs(
            @Parameter(description = "Title of the book") @QueryParam("title") String title,
            @Parameter(description = "Author of the book") @QueryParam("author") String author,
            @Parameter(description = "Tag associated with the book") @QueryParam("tag") String tag) throws Exception {
        return Response.ok(bookService.searchBooksByArgs(title, author, tag)).build();
    }

    @Operation(summary = "Register a new book", description = "Adds a new book to the system")
    @APIResponse(responseCode = "201", description = "Book created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BookDTO.class)))
    @APIResponse(responseCode = "403", description = "Access denied")
    @POST
    @RolesAllowed("Subscriber")
    public Response registerNewBook(@Valid BookDTO bookDTO) throws Exception {
        bookDTO = bookService.persistBook(bookDTO);
        return Response.status(Response.Status.CREATED).entity(bookDTO).build();
    }

    @Operation(summary = "Update a book", description = "Modifies the details of an existing book")
    @APIResponse(responseCode = "200", description = "Book updated successfully")
    @APIResponse(responseCode = "403", description = "Access denied")
    @APIResponse(responseCode = "404", description = "Not found")
    @PUT
    @RolesAllowed("Subscriber")
    public Response updateBook(@Valid BookDTO bookDTO) throws Exception {
        bookService.updateBook(bookDTO);
        return Response.ok().build();
    }

    @Operation(summary = "Delete a book", description = "Removes a book from the system")
    @APIResponse(responseCode = "204", description = "Book deleted successfully")
    @APIResponse(responseCode = "403", description = "Access denied")
    @APIResponse(responseCode = "404", description = "Not found")
    @DELETE
    @Path("/{id}")
    @RolesAllowed("Subscriber")
    public Response deleteBook(@Parameter(description = "UUID of the book", required = true) @PathParam("id") UUID bookId) throws Exception {
        bookService.deleteBook(bookId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
