package com.murilo.market_place.mapper;

import com.murilo.market_place.domains.User;
import com.murilo.market_place.domains.enums.Status;
import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.dtos.user.UserResponseDTO;

public class UserMapper {

    public static User toUser(UserRequestDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .cpf(userDTO.getCpf())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .status(Status.WAITING_ACTIVATION)
                .build();
    }

    public static UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
