package com.murilo.market_place.dtos.creditCard;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public record CreditCardResponseDTO(

        UUID id,
        String number,
        String holderName,
        String cvv,
        LocalDate expirationDate,
        UUID userId

) implements Serializable {
}
