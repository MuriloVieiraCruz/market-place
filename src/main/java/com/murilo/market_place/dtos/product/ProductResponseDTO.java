package com.murilo.market_place.dtos.product;

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
public class ProductResponseDTO implements Serializable {

    private UUID id;
    private String artist;
    private Integer year;
    private String album;
    private BigDecimal price;
    private String store;
    private String thumb;
    private String date;
}
