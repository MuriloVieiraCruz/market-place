package com.murilo.market_place.domains;

import com.murilo.market_place.domains.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table(name = "tb_transaction")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Transaction implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;
    private BigDecimal totalPayment;
    private LocalDateTime moment;
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "transacao", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemTransaction> items;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
