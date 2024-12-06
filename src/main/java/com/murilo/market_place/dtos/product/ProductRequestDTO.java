package com.murilo.market_place.dtos.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO implements Serializable {

        private UUID id;

        @NotBlank(message = "Artist is required")
        private String artist;

        @NotNull(message = "Year is required")
        private Integer year;

        @NotBlank(message = "Album is required")
        private String album;

        @NotNull(message = "Price is required")
        private BigDecimal price;

        @NotBlank(message = "Store is required")
        private String store;

        private Optional<MultipartFile> thumb;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate date;
}
