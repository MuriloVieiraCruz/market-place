package com.murilo.market_place.mapper;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IProductMapper {

    @Mapping(target = "thumb", ignore = true)
    @Mapping(target = "id", ignore = true)
    Product toProduct(ProductRequestDTO productRequestDTO);

    ProductResponseDTO toResponse(Product product);
}
