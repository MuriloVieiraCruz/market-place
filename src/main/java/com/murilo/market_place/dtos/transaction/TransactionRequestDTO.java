package com.murilo.market_place.dtos.transaction;

import com.murilo.market_place.dtos.productcart.ProductCartDTO;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record TransactionRequestDTO(

        UUID userId,
        Integer paymentMethod,
        List<ProductCartDTO> purchaseProducts

) implements Serializable {
}
