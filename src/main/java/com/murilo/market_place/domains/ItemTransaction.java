package com.murilo.market_place.domains;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "tb_item_transaction")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ItemTransaction implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;
    private String productName;
    private Integer quantity;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
