package com.murilo.market_place.mapper;

import com.murilo.market_place.domains.Transaction;
import com.murilo.market_place.dtos.transaction.TransactionResponseDTO;

import java.text.SimpleDateFormat;

public class TransactionMapper {

    public static TransactionResponseDTO toResponse(Transaction transaction) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getTotalPayment(),
                transaction.getItems().stream().map(ItemTransactionMapper::toResponse).toList(),
                formatter.format(transaction.getMoment()),
                transaction.getUser().getId(),
                transaction.getPaymentMethod().getDescription()
        );
    }
}
