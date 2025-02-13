package org.v2com.infra.security;

import java.util.HashMap;

import org.eclipse.microprofile.jwt.Claims;

public class GenerateToken {

    public static void main(String[] args) throws Exception {
        String claimsJson = "/JwtClaims.json";
        if (args.length > 0) {
            claimsJson = args[0];
        }
        HashMap<String, Long> timeClaims = new HashMap<>();
        if (args.length > 1) {
            long duration = Long.parseLong(args[1]);
            long exp = TokenService.currentTimeInSecs() + duration;
            timeClaims.put(Claims.exp.name(), exp);
        }
        String token = TokenService.generateTokenString(claimsJson, timeClaims);
        System.out.println(token);
    }
}