package com.murilo.market_place.dtos.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record ItemTransactionResponseDTO(

        UUID id,
        String productName,
        Integer quantity,
        BigDecimal price

) implements Serializable {
}
