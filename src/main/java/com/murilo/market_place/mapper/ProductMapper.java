package com.murilo.market_place.mapper;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductResponseDTO;

public class ProductMapper {

    public static Product toProduct(ProductRequestDTO productDTO) {
        return Product.builder()
                .artist(productDTO.artist())
                .year(productDTO.year())
                .album(productDTO.album())
                .price(productDTO.price())
                .store(productDTO.store())
                .date(productDTO.date())
                .build();
    }

    public static ProductResponseDTO toResponse(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getArtist(),
                product.getYear(),
                product.getAlbum(),
                product.getPrice(),
                product.getStore(),
                product.getThumb(),
                product.getDate());
    }

    public static Product toUpdatedProduct(ProductRequestDTO productDTO, Product product) {
        return Product.builder()
                .id(productDTO.id())
                .artist(productDTO.artist())
                .year(productDTO.year())
                .album(productDTO.album())
                .price(productDTO.price())
                .store(productDTO.store())
                .thumb(product.getThumb())
                .date(productDTO.date())
                .build();
    }
}
