package com.murilo.market_place.mapper;

import com.murilo.market_place.domains.CreditCard;
import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.dtos.creditCard.CreditCardResponseDTO;

public class CreditCardMapper {

    public static CreditCard toCreditCard(CreditCardRequestDTO creditCardDTO) {
        return CreditCard.builder()
                .id(creditCardDTO.id())
                .number(creditCardDTO.number())
                .holderName(creditCardDTO.holderName())
                .cvv(creditCardDTO.cvv())
                .expirationDate(creditCardDTO.expirationDate())
                .build();
    }

    public static CreditCardResponseDTO toResponse(CreditCard creditCard) {
        return new CreditCardResponseDTO(
                creditCard.getId(),
                creditCard.getNumber(),
                creditCard.getHolderName(),
                creditCard.getCvv(),
                creditCard.getExpirationDate(),
                creditCard.getUser());
    }
}
