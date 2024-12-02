package com.murilo.market_place.repositories;

import com.murilo.market_place.domains.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ICreditCardRepository extends JpaRepository<CreditCard, UUID> {

    List<CreditCard> findAllByUserId(UUID userId);

    CreditCard findByUserId(UUID userId);
}
