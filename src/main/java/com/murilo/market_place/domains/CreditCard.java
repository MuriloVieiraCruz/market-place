package com.murilo.market_place.domains;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
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
    private UUID id;
    private String number;
    private String holderName;
    private String cvv;
    private ZonedDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
