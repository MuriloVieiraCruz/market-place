package com.murilo.market_place.mapper;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductResponseDTO;

public class ProductMapper {

    public static Product toProduct(ProductRequestDTO productDTO) {
        return Product.builder()
                .artist(productDTO.getArtist())
                .year(productDTO.getYear())
                .album(productDTO.getAlbum())
                .price(productDTO.getPrice())
                .store(productDTO.getStore())
                .date(productDTO.getDate())
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
                product.getDate().toString());
    }

    public static Product toUpdatedProduct(ProductRequestDTO productDTO, Product product) {
        return Product.builder()
                .id(productDTO.getId())
                .artist(productDTO.getArtist())
                .year(productDTO.getYear())
                .album(productDTO.getAlbum())
                .price(productDTO.getPrice())
                .store(productDTO.getStore())
                .date(productDTO.getDate())
                .thumb(product.getThumb())
                .build();
    }
}
