package org.v2com.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.v2com.dto.LoginDTO;
import org.v2com.service.AuthService;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) throws Exception {
        String token = authService.authenticate(loginDTO.username, loginDTO.password);
        if (token == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"Invalid credentials\"}").build();
        }
        return Response.ok("{\"token\": \"" + token + "\"}").build();
    }
}
