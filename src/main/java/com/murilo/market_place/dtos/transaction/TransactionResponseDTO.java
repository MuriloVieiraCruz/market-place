package com.murilo.market_place.dtos.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO implements Serializable {

    private UUID id;
    private BigDecimal TotalPayment;
    private List<ItemTransactionResponseDTO> items;
    private String moment;
    private UUID userId;
    private String paymentMethod;
}
