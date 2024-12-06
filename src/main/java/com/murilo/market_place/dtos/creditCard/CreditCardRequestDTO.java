package com.murilo.market_place.dtos.creditCard;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardRequestDTO implements Serializable {

        private UUID id;

        @NotBlank(message = "The card number is required")
        @Size(min = 16, max = 16, message = "The card number must contain 16 digits")
        private String number;

        @NotBlank(message = "The holder name is required")
        private String holderName;

        @NotNull(message = "The CVV is required")
        @Size(min = 3, max = 3, message = "The CVV must have at least 3 digits")
        private String cvv;

        @NotNull(message = "The expiration date is required")
        @Future(message = "The credit card is expired")
        private LocalDate expirationDate;

        @NotNull(message = "The user is required")
        private UUID userId;
}
