package com.murilo.market_place.factory;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
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
                "Pink Floyd",
                1973,
                "Dask Side of The Moon",
                BigDecimal.valueOf(61.90),
                "Vinil Records",
                getFileInstance(),
                LocalDate.now()
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