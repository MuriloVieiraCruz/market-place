package com.murilo.market_place.controllers;

import com.murilo.market_place.dtos.security.AuthorizationRequestDTO;
import com.murilo.market_place.dtos.security.TokenRequestDTO;
import com.murilo.market_place.dtos.security.TokenResponseDTO;
import com.murilo.market_place.services.AuthorizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody AuthorizationRequestDTO authRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authorizationService.login(authRequest));
    }

    @PostMapping
    public ResponseEntity<TokenResponseDTO> getToken(@RequestBody TokenRequestDTO tokenDTO) {
        return ResponseEntity.ok(authorizationService.getByRefreshToken(tokenDTO));
    }
}
