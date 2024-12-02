package com.murilo.market_place.controllers;

import com.murilo.market_place.controllers.documentation.ICreditCardDocController;
import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.dtos.creditCard.CreditCardResponseDTO;
import com.murilo.market_place.mapper.CreditCardMapper;
import com.murilo.market_place.services.CreditCardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@AllArgsConstructor
public class CreditCardController implements ICreditCardDocController {

    private final CreditCardService creditCardService;

    @Override
    public ResponseEntity<CreditCardResponseDTO> addCreditCard(CreditCardRequestDTO creditCardRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CreditCardMapper.toResponse(creditCardService.saveCard(creditCardRequestDTO)));
    }

    @Override
    public ResponseEntity<List<CreditCardResponseDTO>> listAllCardsFromUser(UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(creditCardService.findAllCardsByUser(userId).stream()
                .map(CreditCardMapper::toResponse)
                .toList()
        );
    }

    @Override
    public ResponseEntity<CreditCardResponseDTO> findCardById(UUID cardId) {
        return ResponseEntity.status(HttpStatus.OK).body(CreditCardMapper.toResponse(creditCardService.findCardById(cardId)));
    }

    @Override
    public ResponseEntity<Void> deleteCardById(UUID cardId) {
        creditCardService.delete(cardId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
