package com.murilo.market_place.services;

import com.murilo.market_place.configs.security.TokenGeneratorConfig;
import com.murilo.market_place.dtos.security.AuthorizationRequestDTO;
import com.murilo.market_place.dtos.security.TokenRequestDTO;
import com.murilo.market_place.dtos.security.TokenResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenGeneratorConfig tokenGeneratorConfig;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthenticationProvider")
    private JwtAuthenticationProvider refreshTokenAuthProvider;

    public TokenResponseDTO login(AuthorizationRequestDTO authRequest) {
        UserDetails user = userDetailService.loadUserByUsername(authRequest.getUsername());

        if (!verifyPassword(authRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Username or password is invalid");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        return tokenGeneratorConfig.generateToken(authentication);
    }

    private boolean verifyPassword(String password, String passwordEncrypted) {
        return passwordEncoder.matches(password, passwordEncrypted);
    }

    public TokenResponseDTO getByRefreshToken(TokenRequestDTO tokenRequestDTO) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenRequestDTO.getRefreshToken()));
        return tokenGeneratorConfig.generateToken(authentication);
    }
}
