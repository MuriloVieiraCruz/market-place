package com.murilo.market_place.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements Serializable {

    private UUID id;
    private String name;
    private String cpf;
    private String email;
    private String password;
}
