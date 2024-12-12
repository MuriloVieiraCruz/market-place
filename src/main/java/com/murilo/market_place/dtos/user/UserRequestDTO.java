package com.murilo.market_place.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO implements Serializable {

        private UUID id;

        @NotBlank(message = "Name is required")
        private String name;

        @CPF(message = "Cpf is in an invalid format")
        @Size(max = 11, min = 11, message = "Cpf need to have 11 characters")
        @NotBlank(message = "Cpf is required")
        private String cpf;

        @Email(message = "Email is in an invalid format")
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
}
