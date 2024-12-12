package com.murilo.market_place.domains;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "tb_role")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private UUID id;

    @Column(name = "role_name")
    private String name;
}
