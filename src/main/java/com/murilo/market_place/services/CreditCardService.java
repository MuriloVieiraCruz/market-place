package com.murilo.market_place.services;

import com.murilo.market_place.domains.CreditCard;
import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.mapper.CreditCardMapper;
import com.murilo.market_place.repositories.ICreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditCardService {

    private final ICreditCardRepository creditCardRepository;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    public CreditCard create(CreditCardRequestDTO creditCardRequestDTO) {
        CreditCard creditCard = CreditCardMapper.toCreditCard(creditCardRequestDTO);

        //TODO implementar busca do usuario pelo token recebido
        // não precisando passar ele por parâmetro
        creditCard.setUser(userService.findById(creditCardRequestDTO.getUserId()));

        //TODO utilizar API para verificar validade e informações do cartão
        return creditCardRepository.save(creditCard);
    }

    @Transactional(readOnly = true)
    public List<CreditCard> findAllCardsByUser(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new NullInsertValueException("ID is required for product search");
        }

        return creditCardRepository.findAllByUserId(userId);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "cardCache", key = "#cardId")
    public CreditCard findById(UUID cardId) {
        return findCard(cardId);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "cardCache", key = "#cardId")
    public void deleteById(UUID cardId) {
        if (cardId != null) {
            existsCreditCard(cardId);
            creditCardRepository.deleteById(cardId);
        } else {
            throw new NullInsertValueException("ID is required for card removal");
        }
    }

    private CreditCard findCard(UUID cardId) {
        if (Objects.isNull(cardId)) {
            throw new NullInsertValueException("ID is required for card search");
        }

        return creditCardRepository.findById(cardId).orElseThrow(() -> new EntityNotFoundException(CreditCard.class));
    }

    @Transactional(readOnly = true)
    private void existsCreditCard(UUID creditCardId) {
        if (creditCardId != null) {
            boolean exist = creditCardRepository.existsById(creditCardId);
            if (!exist) {
                throw new EntityNotFoundException(CreditCard.class);
            }
        } else {
            throw new NullInsertValueException("ID is required for card removal");
        }
    }
}
