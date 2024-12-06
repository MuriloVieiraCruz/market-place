package com.murilo.market_place.dtos.creditCard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardResponseDTO implements Serializable {

    private UUID id;
    private String number;
    private String holderName;
    private String cvv;
    private String expirationDate;
    private UUID userId;
}
