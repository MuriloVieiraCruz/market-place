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
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_cpf")
    private String cpf;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_status")
    private Status status;

    //TODO implement roles
}
