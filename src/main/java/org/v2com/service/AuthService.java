package org.v2com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.Claims;
import org.mindrot.jbcrypt.BCrypt;
import org.v2com.dto.UserDTO;
import org.v2com.exceptions.UserNotFoundException;
import org.v2com.infra.security.TokenService;

import java.util.HashMap;

@ApplicationScoped
public class AuthService {

    @Inject
    UserService userService;

    public String authenticate(String username, String password) throws Exception {
        UserDTO user = userService.finUserByName(username);

        if (user == null) { return null;}

        if (!user.getName().equals(username) || !checkPassword(password, user.getPassword())) {
            return null;
        }

         // Gerar o token JWT
        HashMap<String, Long> timeClaims = new HashMap<>();
        long exp = TokenService.currentTimeInSecs() + 3600;
        timeClaims.put(Claims.exp.name(), exp);

        return TokenService.generateTokenString("/JwtClaims.json", timeClaims);
    }

    // gerar hash da senha para armazenar
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // verificar hash armazenado
    public static boolean checkPassword(String password, String storedHash) {
        return BCrypt.checkpw(password, storedHash);
    }
}