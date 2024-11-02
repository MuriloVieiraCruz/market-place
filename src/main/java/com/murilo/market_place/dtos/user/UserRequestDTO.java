package com.murilo.market_place.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.util.UUID;

public record UserRequestDTO(

        UUID id,

        @NotBlank(message = "Name is required")
        String name,

        @CPF(message = "Cpf is in an invalid format")
        @NotBlank(message = "Cpf is required")
        String cpf,

        @Email(message = "Email is in an invalid format")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password
) implements Serializable {
}
