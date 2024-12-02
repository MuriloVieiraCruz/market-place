package com.murilo.market_place.dtos.creditCard;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

public record CreditCardRequestDTO(

        UUID id,

        @NotBlank(message = "The card number is required")
        String number,

        @NotBlank(message = "The holder name is required")
        String holderName,

        @NotNull(message = "The CVV is required")
        @Size(min = 3, max = 3, message = "The CVV must have at least 3 digits")
        String cvv,

        @NotNull(message = "The expiration date is required")
        @Future(message = "The credit card is expired")
        ZonedDateTime expirationDate,

        @NotNull(message = "The user is required")
        UUID userId

) implements Serializable {
}
