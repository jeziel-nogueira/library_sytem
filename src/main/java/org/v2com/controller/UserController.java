package org.v2com.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.v2com.dto.LoginDTO;
import org.v2com.dto.UserDTO;
import org.v2com.service.AuthService;
import org.v2com.service.UserService;

import java.util.List;
import java.util.UUID;

@Path("/api/v1/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    UserService userService;

    @Inject
    AuthService authService;

    @Operation(summary = "User login", description = "Authenticates a user and returns a token")
    @APIResponse(responseCode = "200", description = "User authenticated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "401", description = "Invalid credentials")
    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) throws Exception {
        String token = authService.authenticate(loginDTO.username, loginDTO.password);
        if (token == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"Invalid credentials\"}").build();
        }
        return Response.ok("{\"token\": \"" + token + "\"}").build();
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @APIResponse(responseCode = "200", description = "List of users", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "204", description = "Not found")
    @APIResponse(responseCode = "401", description = "Access denied")
    @GET
    @RolesAllowed("Subscriber")
    @Path("/all")
    public Response getAllUsers() throws Exception {
        List<UserDTO> users = userService.listAllUsers();
        return !users.isEmpty()
                ? Response.ok(users).build()
                : Response.status(Response.Status.NO_CONTENT).build();
    }

    @Operation(summary = "Get user by ID", description = "Fetches a user by their unique identifier")
    @APIResponse(responseCode = "200", description = "User details", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "404", description = "Not found")
    @APIResponse(responseCode = "401", description = "Access denied")
    @GET
    @RolesAllowed("Subscriber")
    @Path("/id/{id}")
    public Response getUserById(@Parameter(description = "UUID of the user", required = true) @PathParam("id") UUID userId) throws Exception {
        UserDTO user = userService.findUserById(userId);
        return Response.ok(user).build();
    }

    @Operation(summary = "Get user by name", description = "Fetches a user by their name")
    @APIResponse(responseCode = "200", description = "User details", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "404", description = "Not found")
    @APIResponse(responseCode = "401", description = "Access denied")
    @GET
    @RolesAllowed("Subscriber")
    @Path("/name/{name}")
    public Response getUserByname(@Parameter(description = "Name of the user", required = true) @PathParam("name") String userName) throws Exception {
        UserDTO user = userService.finUserByName(userName);
        return Response.ok(user).build();
    }

    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @APIResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "401", description = "Access denied")
    @POST
    @RolesAllowed("Subscriber")
    public Response createUser(@Valid UserDTO userDTO) throws Exception {
        UserDTO newUser = userService.addUser(userDTO);
        return Response.ok(newUser).build();
    }

    @Operation(summary = "Update user", description = "Updates user information")
    @APIResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "404", description = "Not found")
    @APIResponse(responseCode = "401", description = "Access denied")
    @PUT
    @RolesAllowed("Subscriber")
    public Response updateUser(@Valid UserDTO userDTO) throws Exception {
        return Response.ok(userService.updateUser(userDTO)).build();
    }

    @Operation(summary = "Delete user by ID", description = "Removes a user from the system by their unique identifier")
    @APIResponse(responseCode = "204", description = "User deleted successfully")
    @APIResponse(responseCode = "404", description = "Not found")
    @APIResponse(responseCode = "401", description = "Access denied")
    @DELETE
    @RolesAllowed("Subscriber")
    @Path("/{id}")
    public Response deleUserById(@Parameter(description = "UUID of the user", required = true) @PathParam("id") UUID userId) throws Exception {
        userService.deleteUserById(userId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}