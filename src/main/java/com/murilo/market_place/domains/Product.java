package com.murilo.market_place.domains;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table(name = "tb_product")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Product implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private UUID id;

    @Column(name = "product_artist")
    private String artist;

    @Column(name = "product_year")
    private Integer year;

    @Column(name = "product_album")
    private String album;

    @Column(name = "product_price")
    private BigDecimal price;

    @Column(name = "product_store")
    private String store;

    @Column(name = "product_thumb")
    private String thumb;

    @Column(name = "product_date")
    private LocalDate date;

}
