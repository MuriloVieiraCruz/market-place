package com.murilo.market_place.dtos.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
    //private long expirationDate;
}
