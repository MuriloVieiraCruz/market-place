package com.murilo.market_place.repositories;

import com.murilo.market_place.domains.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ITransactionRepository extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findByUserId(UUID userId, Pageable pageable);

    @EntityGraph(attributePaths = {"items"})
    Optional<Transaction> findWithItemsById(UUID transactionId);
}
