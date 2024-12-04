package com.murilo.market_place.mapper;

import com.murilo.market_place.domains.ItemTransaction;
import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.productcart.ProductCartDTO;
import com.murilo.market_place.dtos.transaction.ItemTransactionResponseDTO;

public class ItemTransactionMapper {

    public static ItemTransaction toItemTransaction(ProductCartDTO productCartDTO) {
        return ItemTransaction.builder()
                .productName(productCartDTO.productName())
                .quantity(productCartDTO.quantity())
                .price(productCartDTO.price())
                .product(
                        Product.builder()
                                .id(productCartDTO.productId())
                                .album(productCartDTO.productName())
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
