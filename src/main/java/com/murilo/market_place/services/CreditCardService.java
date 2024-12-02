package com.murilo.market_place.services;

import com.murilo.market_place.domains.CreditCard;
import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullValueInsertionException;
import com.murilo.market_place.mapper.CreditCardMapper;
import com.murilo.market_place.repositories.ICreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditCardService {

    private final ICreditCardRepository creditCardRepository;
    private final UserService userService;

    public CreditCard saveCard(CreditCardRequestDTO creditCardRequestDTO) {
        CreditCard creditCard = CreditCardMapper.toCreditCard(creditCardRequestDTO);

        //TODO implementar busca do usuario pelo token recebido
        // não precisando passar ele por parâmetro
        creditCard.setUser(userService.findById(creditCardRequestDTO.userId()));

        //TODO utilizar API para verificar validade e informações do cartão
        return creditCardRepository.save(creditCard);
    }

    public List<CreditCard> findAllCardsByUser(UUID userId) {

        if (Objects.isNull(userId)) {
            throw new NullValueInsertionException("ID is required for product search");
        }

        return creditCardRepository.findAllByUserId(userId);
    }

    public CreditCard findCardById(UUID cardId) {
        return findCard(cardId);
    }

    public void delete(UUID cardId) {
        if (cardId != null) {
            try {
                creditCardRepository.deleteById(cardId);
            } catch (EmptyResultDataAccessException e) {
                throw new EntityNotFoundException(Product.class);
            }
        } else {
            throw new NullValueInsertionException("ID is required for card removal");
        }
    }

    private CreditCard findCard(UUID cardId) {
        if (Objects.isNull(cardId)) {
            throw new NullValueInsertionException("ID is required for card search");
        }

        return creditCardRepository.findById(cardId).orElseThrow(() -> new EntityNotFoundException(CreditCard.class));
    }
}
