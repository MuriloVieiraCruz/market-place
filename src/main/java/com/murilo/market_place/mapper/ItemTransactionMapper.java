package com.murilo.market_place.mapper;

import com.murilo.market_place.domains.ItemTransaction;
import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.productcart.ProductCartDTO;
import com.murilo.market_place.dtos.transaction.ItemTransactionResponseDTO;

public class ItemTransactionMapper {

    public static ItemTransaction toItemTransaction(ProductCartDTO productCartDTO) {
        return ItemTransaction.builder()
                .productName(productCartDTO.getProductName())
                .quantity(productCartDTO.getQuantity())
                .price(productCartDTO.getPrice())
                .product(
                        Product.builder()
                                .id(productCartDTO.getProductId())
                                .album(productCartDTO.getProductName())
                                .build()
                )
                .build();
    }

    public static ItemTransactionResponseDTO toResponse(ItemTransaction itemTransaction) {
        return new ItemTransactionResponseDTO(
                itemTransaction.getId(),
                itemTransaction.getProductName(),
                itemTransaction.getQuantity(),
                itemTransaction.getPrice());
    }
}
