package com.murilo.market_place.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private UUID id;
    private String artist;
    private Integer year;
    private String album;
    private BigDecimal price;
    private String store;
    private String thumb;
    private LocalDate date;

}
