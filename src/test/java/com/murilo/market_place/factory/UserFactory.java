package com.murilo.market_place.factory;

import com.murilo.market_place.domains.User;
import com.murilo.market_place.domains.enums.Status;
import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.dtos.user.UserResponseDTO;

import java.util.UUID;

public class UserFactory {

    public static User getUserInstance() {
        return User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .cpf("12616899094")
                .email("emailteste@gmail.com")
                .password("password_123")
                .status(Status.WAITING_ACTIVATION)
                .build();
    }

    public static UserRequestDTO getUserRequestInstance() {
        return new UserRequestDTO(
                null,
                "Test User",
                "12616899094",
                "emailteste@gmail.com",
                "password_123"
        );
    }

    public static UserRequestDTO getUserUpdateInstance() {
        return new UserRequestDTO(
                UUID.randomUUID(),
                "Test User",
                "12616899094",
                "emailteste@gmail.com",
                "password_123"
        );
    }

    public static UserResponseDTO getUserResponseInstance() {
        return new UserResponseDTO(
                UUID.randomUUID(),
                "Test User",
                "12616899094",
                "emailteste@gmail.com",
                "password_123"
        );
    }

}
