package org.v2com.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.v2com.Enums.ReserveStatus;
import org.v2com.dto.ReservationDTO;
import org.v2com.service.ReserveService;

import java.util.List;
import java.util.UUID;

@Path("/api/v1/reserve")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReserveController {

    @Inject
    ReserveService reserveService;

    @Operation(summary = "View all reservations", description = "Retrieves a list of all reservations")
    @APIResponse(responseCode = "200", description = "List of reservations", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "204", description = "Not found")
    @APIResponse(responseCode = "403", description = "Access denied")
    @GET
    @RolesAllowed("Subscriber")
    public Response getAllReserves() throws Exception {
        List<ReservationDTO> reservations = reserveService.getAllReservations();
        return reservations.isEmpty() ?
                Response.status(Response.Status.NO_CONTENT).build()
                : Response.ok(reservations).build();
    }

    @Operation(summary = "Get reservation by ID", description = "Fetches a reservation using its unique identifier")
    @APIResponse(responseCode = "200", description = "Reservation details", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "404", description = "Not found")
    @APIResponse(responseCode = "403", description = "Access denied")
    @GET
    @Path("/{id}")
    @RolesAllowed("Subscriber")
    public Response getReserveById(@Parameter(description = "UUID of the reservation", required = true) @PathParam("id") UUID reserveId) throws Exception {
        return Response.ok(reserveService.getReserveById(reserveId)).build();
    }

    @Operation(summary = "Get active reservation by book ID", description = "Fetches the active reservation for a specific book")
    @APIResponse(responseCode = "200", description = "Reservation details", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "404", description = "Not found")
    @APIResponse(responseCode = "403", description = "Access denied")
    @GET
    @Path("/book/{id}")
    @RolesAllowed("Subscriber")
    public Response getActiveReserveByBookId(@Parameter(description = "UUID of the book", required = true) @PathParam("id") UUID bookId) throws Exception {
        return Response.ok(reserveService.getActiveReserveByBookId(bookId)).build();
    }

    @Operation(summary = "Get active reservation by user ID", description = "Fetches the active reservation for a specific user")
    @APIResponse(responseCode = "200", description = "Reservation details", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "404", description = "Not found")
    @APIResponse(responseCode = "403", description = "Access denied")
    @GET
    @Path("/user/{id}")
    @RolesAllowed("Subscriber")
    public Response getActiveReserveByUserId(@Parameter(description = "UUID of the user", required = true) @PathParam("id") UUID userId) throws Exception {
        return Response.ok(reserveService.getActiveReserveByUserId(userId)).build();
    }

    @Operation(summary = "Create a new reservation", description = "Creates a new reservation for a book and user")
    @APIResponse(responseCode = "201", description = "Reservation created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "403", description = "Access denied")
    @POST
    @RolesAllowed("Subscriber")
    public Response createReserve(@QueryParam("userId") UUID userId, @QueryParam("bookId") UUID bookId) throws Exception {
        return Response.ok(reserveService.createReserve(bookId, userId)).build();
    }

    @Operation(summary = "Change reservation status", description = "Updates the status of a reservation")
    @APIResponse(responseCode = "200", description = "Reservation status updated successfully")
    @APIResponse(responseCode = "404", description = "Not found")
    @APIResponse(responseCode = "403", description = "Access denied")
    @PUT
    @RolesAllowed("Subscriber")
    public Response changeReserveStatus(@QueryParam("reserveId") UUID reserveId, @QueryParam("status") ReserveStatus status) throws Exception {
        return Response.ok(reserveService.changeReserveStatus(reserveId, status)).build();
    }
}
