package com.murilo.market_place.dtos.transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record TransactionResponseDTO(

        UUID id,
        BigDecimal TotalPayment,
        List<ItemTransactionResponseDTO> items,
        String moment,
        UUID userId,
        String paymentMethod
) {
}
