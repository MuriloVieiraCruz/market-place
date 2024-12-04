package com.murilo.market_place.repositories;

import com.murilo.market_place.domains.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ITransactionRepository extends JpaRepository<Transaction, UUID> {
}
