package com.murilo.market_place.dtos.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProductResponseDTO(

        UUID id,
        String artist,
        Integer year,
        String album,
        BigDecimal price,
        String store,
        String thumb,
        LocalDate date
) implements Serializable {
}
