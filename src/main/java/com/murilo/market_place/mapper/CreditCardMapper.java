package com.murilo.market_place.mapper;

import com.murilo.market_place.domains.CreditCard;
import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.dtos.creditCard.CreditCardResponseDTO;

public class CreditCardMapper {

    public static CreditCard toCreditCard(CreditCardRequestDTO creditCardDTO) {
        return CreditCard.builder()
                .id(creditCardDTO.getId())
                .number(creditCardDTO.getNumber())
                .holderName(creditCardDTO.getHolderName())
                .cvv(creditCardDTO.getCvv())
                .expirationDate(creditCardDTO.getExpirationDate())
                .build();
    }

    public static CreditCardResponseDTO toResponse(CreditCard creditCard) {
        return new CreditCardResponseDTO(
                creditCard.getId(),
                creditCard.getNumber(),
                creditCard.getHolderName(),
                creditCard.getCvv(),
                creditCard.getExpirationDate().toString(),
                creditCard.getUser().getId());
    }
}
