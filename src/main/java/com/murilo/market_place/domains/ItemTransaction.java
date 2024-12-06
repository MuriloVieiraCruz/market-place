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
    @Column(name = "item_transaction_id")
    private UUID id;

    @Column(name = "item_transaction_product_name")
    private String productName;

    @Column(name = "item_transaction_quantity")
    private Integer quantity;

    @Column(name = "item_transaction_price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
