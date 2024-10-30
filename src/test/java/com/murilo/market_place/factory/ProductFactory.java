package com.murilo.market_place.factory;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductUpdateRequestDTO;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class ProductFactory {

    public static Product getProductInstance() {
        return new Product(
                UUID.randomUUID(),
                "Pink Floyd",
                1973,
                "Dask Side of The Moon",
                BigDecimal.valueOf(61.90),
                "Vinil Records",
                "https://images-na.ssl-images-amazon.com/images/I/61R7gJadP7L._SX355_.jpg",
                LocalDate.now()
        );
    }

    public static ProductRequestDTO getProductRequestInstance() {
        return new ProductRequestDTO(
                UUID.randomUUID(),
                "Pink Floyd",
                1973,
                "Dask Side of The Moon",
                BigDecimal.valueOf(61.90),
                "Vinil Records",
                getFileInstance(),
                LocalDate.now()
        );
    }

    public static ProductUpdateRequestDTO getProductUpdateRequestInstance() {
        return new ProductUpdateRequestDTO(
                UUID.randomUUID(),
                Optional.of("Pink Floyd"),
                Optional.of(1973),
                Optional.of("Dask Side of The Moon"),
                Optional.of(BigDecimal.valueOf(61.90)),
                Optional.of("Vinil Records"),
                Optional.of(getFileInstance()),
                Optional.of(LocalDate.now())
        );
    }

    private static MockMultipartFile getFileInstance() {
        return new MockMultipartFile(
                "file",
                "darkSide.jpg",
                "image/jpeg",
                "ArchiveContent".getBytes());
    }
}