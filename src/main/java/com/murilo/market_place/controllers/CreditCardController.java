package com.murilo.market_place.controllers;

import com.murilo.market_place.controllers.documentation.ICreditCardDocController;
import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.dtos.creditCard.CreditCardResponseDTO;
import com.murilo.market_place.mapper.CreditCardMapper;
import com.murilo.market_place.services.CreditCardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@AllArgsConstructor
public class CreditCardController implements ICreditCardDocController {

    private final CreditCardService creditCardService;

    @PostMapping("/add")
    public ResponseEntity<CreditCardResponseDTO> addCreditCard( @RequestBody @Valid CreditCardRequestDTO creditCardRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CreditCardMapper.toResponse(creditCardService.create(creditCardRequestDTO)));
    }

    @GetMapping("/search/{userId}")
    public ResponseEntity<List<CreditCardResponseDTO>> listAllCardsFromUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(creditCardService.findAllCardsByUser(userId).stream()
                .map(CreditCardMapper::toResponse)
                .toList()
        );
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CreditCardResponseDTO> findCardById(@PathVariable("cardId") UUID cardId) {
        return ResponseEntity.status(HttpStatus.OK).body(CreditCardMapper.toResponse(creditCardService.findById(cardId)));
    }

    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<Void> deleteCardById(@PathVariable("cardId") UUID cardId) {
        creditCardService.deleteById(cardId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
