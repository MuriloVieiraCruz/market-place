package com.murilo.market_place.configs.security;

import com.murilo.market_place.dtos.security.TokenResponseDTO;
import com.murilo.market_place.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
public class TokenGeneratorConfig {

    @Autowired
    private JwtEncoder accessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshEncoder")
    private JwtEncoder refreshTokenEncoder;

    public TokenResponseDTO generateToken(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = determineRefreshToken(authentication);

        return new TokenResponseDTO(accessToken, refreshToken);
    }

    private String determineRefreshToken(Authentication authentication) {
        Jwt jwt = JwtUtils.extractJwt(authentication);
        if (jwt != null) {
            return handleJwtRefreshToken(jwt, authentication);
        }

        return generateRefreshToken(authentication);
    }

    private String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, 7000, refreshTokenEncoder);
    }

    private String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, 5, accessTokenEncoder);
    }

    private String handleJwtRefreshToken(Jwt jwt, Authentication userAuthenticated) {
        Instant now = Instant.now();
        Instant expiresAt = jwt.getExpiresAt();
        long daysToExpire = Duration.between(now, expiresAt).toDays();

        if (daysToExpire >= 7) {
            return generateRefreshToken(userAuthenticated);
        } else {
            return jwt.getTokenValue();
        }
    }

    private String generateToken(Authentication userAuthenticated, long durationMinutes, JwtEncoder encoder) {
        Instant now = Instant.now();

        String roles = userAuthenticated.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> "ROLE_" + role)
                .collect(Collectors.joining(","));

        var claims = JwtClaimsSet.builder()
                .issuer("market-place")
                .issuedAt(now)
                .expiresAt(now.plus(durationMinutes, ChronoUnit.MINUTES))
                .subject(userAuthenticated.getName())
                .claim("roles", roles)
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
