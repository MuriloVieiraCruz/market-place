package com.murilo.market_place.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtUtils {

    public static Jwt extractJwt(Authentication authentication) {
        if (authentication == null || authentication.getCredentials() == null) {
            return null;
        }
        if (authentication.getCredentials() instanceof Jwt) {
            return (Jwt) authentication.getCredentials();
        }
        return null;
    }
}
