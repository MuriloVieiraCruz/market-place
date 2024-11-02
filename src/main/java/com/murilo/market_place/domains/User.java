package com.murilo.market_place.domains;

import com.murilo.market_place.domains.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Table(name = "tb_user")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String cpf;
    private String email;
    private String password;
    private Status status;

    //TODO implement credit cards
    //TODO implement roles
}
