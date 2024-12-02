package com.murilo.market_place.factory;

import com.murilo.market_place.domains.CreditCard;
import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.dtos.creditCard.CreditCardResponseDTO;

import java.time.LocalDate;
import java.util.UUID;

public class CreditCardFactory {

    public static CreditCard getCardInstance() {
        return CreditCard.builder()
                .id(UUID.randomUUID())
                .number("1234567891011123")
                .holderName("Usuario teste")
                .cvv("555")
                .expirationDate(LocalDate.now().plusDays(1))
                .user(UserFactory.getUserInstance())
                .build();
    }

    public static CreditCardRequestDTO getCardRequestInstance() {
        return new CreditCardRequestDTO(
                null,
                "1234567891011123",
                "Usuario teste",
                "555",
                LocalDate.now().plusDays(1),
                UUID.randomUUID()
        );
    }

    public static CreditCardResponseDTO getCardResponseInstance() {
        return new CreditCardResponseDTO(
                UUID.randomUUID(),
                "1234567891011123",
                "Usuario teste",
                "555",
                LocalDate.now().plusDays(1),
                UUID.randomUUID()
        );
    }
}
