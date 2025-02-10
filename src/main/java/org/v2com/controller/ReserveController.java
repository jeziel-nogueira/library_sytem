package org.v2com.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.v2com.Enums.ReserveStatus;
import org.v2com.service.ReserveService;

import java.util.UUID;

@Path("/api/v1/reserve")
public class ReserveController {

    @Inject
    ReserveService reserveService;

    @GET
    @Path("/")
    public Response getAllReserves(){
        return Response.ok(reserveService.getAllReservations()).build();
    }

    @GET
    @Path("/{id}")
    public Response getReserveById(@PathParam("id")UUID reserveId){
        return Response.ok(reserveService.getReserveById(reserveId)).build();
    }

    @GET
    @Path("/book/{id}")
    public Response getActiveReserveByBookId(@PathParam("id")UUID bookId){
        return Response.ok(reserveService.getActiveReserveByBookId(bookId)).build();
    }

    @GET
    @Path("/user/{id}")
    public Response getActiveReserveByUserId(@PathParam("id")UUID userId){
        return Response.ok(reserveService.getActiveReserveByUserId(userId)).build();
    }

    @POST
    @Path("/")
    public Response createReserve(@QueryParam("userId")UUID bookId, @QueryParam("bookId")UUID userId) throws Exception {
        return Response.ok(reserveService.createReserve(bookId, userId)).build();
    }

    @PUT
    @Path("/")
    public Response changeReserveStatus(@QueryParam("reserveId") UUID reserveId, @QueryParam("status") ReserveStatus status){
        return Response.ok(reserveService.changeReserveStatus(reserveId, status)).build();
    }
}
