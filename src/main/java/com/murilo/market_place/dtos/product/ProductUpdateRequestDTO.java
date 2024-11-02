package com.murilo.market_place.dtos.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record ProductUpdateRequestDTO(
        UUID id,

        @NotBlank(message = "Artist is required")
        String artist,

        @NotNull(message = "Year is required")
        Integer year,

        @NotBlank(message = "Album is required")
        String album,

        @NotNull(message = "Price is required")
        BigDecimal price,

        @NotBlank(message = "Store is required")
        String store,

        Optional<MultipartFile> thumb,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date
) {
}
