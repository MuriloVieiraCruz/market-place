package com.murilo.market_place.dtos.transaction;

import com.murilo.market_place.dtos.productcart.ProductCartDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO implements Serializable {

    private UUID userId;
    private Integer paymentMethod;
    private List<ProductCartDTO> purchaseProducts;
}
