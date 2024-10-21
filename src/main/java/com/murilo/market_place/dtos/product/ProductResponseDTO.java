package com.murilo.market_place.dtos.product;

import java.util.Date;
import java.util.UUID;

public record ProductResponseDTO(

        UUID id,
        String artist,
        Integer year,
        String album,
        Double price,
        String store,
        String thumb,
        Date date
) {
}
