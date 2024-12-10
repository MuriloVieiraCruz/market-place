package com.murilo.market_place.mapper;

import com.murilo.market_place.domains.Transaction;
import com.murilo.market_place.dtos.transaction.TransactionResponseDTO;

public class TransactionMapper {

    public static TransactionResponseDTO toResponse(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getTotalPayment(),
                transaction.getItems().stream().map(ItemTransactionMapper::toResponse).toList(),
                transaction.getMoment().toString(),
                transaction.getUser().getId(),
                transaction.getPaymentMethod().getDescription()
        );
    }
}
