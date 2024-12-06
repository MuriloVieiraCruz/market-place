package com.murilo.market_place.domains;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "tb_credit_card")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CreditCard {

    @Id
    @GeneratedValue
    @Column(name = "card_id")
    private UUID id;

    @Column(name = "card_number")
    private String number;

    @Column(name = "card_holder_name")
    private String holderName;

    @Column(name = "card_cvv")
    private String cvv;

    @Column(name = "card_expiration_date")
    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
