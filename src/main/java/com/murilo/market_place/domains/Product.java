package com.murilo.market_place.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Table(name = "tb_product")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity implements Serializable {

    private String artist;
    private Integer year;
    private String album;
    private Double price;
    private String store;
    private String thumb;
    private Date date;
}
