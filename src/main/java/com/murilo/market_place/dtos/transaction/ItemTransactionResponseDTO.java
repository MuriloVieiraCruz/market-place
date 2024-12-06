package com.murilo.market_place.dtos.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemTransactionResponseDTO implements Serializable {

    private UUID id;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
