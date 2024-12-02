package com.murilo.market_place.dtos.creditCard;

import com.murilo.market_place.domains.User;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

public record CreditCardResponseDTO(

        UUID id,
        String number,
        String holderName,
        String cvv,
        ZonedDateTime expirationDate,
        User user

) implements Serializable {
}
