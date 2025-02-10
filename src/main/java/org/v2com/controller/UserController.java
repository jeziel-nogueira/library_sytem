package org.v2com.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.v2com.dto.UserDTO;
import org.v2com.service.UserService;

import java.util.List;
import java.util.UUID;

@Path("/api/v1/user")
public class UserController {
    @Inject
    UserService userService;

    @GET
    @Path("/")
    public Response getAllUsers() throws Exception {
        List<UserDTO> users = userService.listAllUsers();
        return users != null || users.isEmpty()
                ? Response.ok(users).build()
                : Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/id/{id}")
    public Response getUserById(@PathParam("id")UUID userId) throws Exception {
        UserDTO user = userService.findUserById(userId);
        return Response.ok(user).build();
    }

    @GET
    @Path("/name/{name}")
    public Response getUserByname(@PathParam("name")String userName) throws Exception {
        UserDTO user = userService.finUserByName(userName);
        return Response.ok(user).build();
    }

    @POST
    @Path("/register")
    public Response createUser(@Valid UserDTO userDTO) throws Exception {
        UserDTO newUser = userService.addUser(userDTO);
        return Response.ok(newUser).build();
    }

    @PUT
    @Path("/update")
    public Response updateUser(@Valid UserDTO userDTO) throws Exception {
        return Response.ok(userService.updateUser(userDTO)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleUserById(@PathParam("id") UUID userId){
        userService.deleteUserById(userId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
