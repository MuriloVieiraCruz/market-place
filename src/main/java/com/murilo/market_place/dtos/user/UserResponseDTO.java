package com.murilo.market_place.dtos.user;

import java.io.Serializable;
import java.util.UUID;

public record UserResponseDTO(

        UUID id,
        String name,
        String cpf,
        String email,
        String password
) implements Serializable {
}
