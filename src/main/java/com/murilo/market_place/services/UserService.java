package com.murilo.market_place.services;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.domains.User;
import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.dtos.user.UserResponseDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullValueInsertionException;
import com.murilo.market_place.mapper.UserMapper;
import com.murilo.market_place.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = UserMapper.toUser(userRequestDTO);

        //TODO Encrypt user password here
        user.setPassword(userRequestDTO.password());

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO) {
        if (Objects.isNull(userRequestDTO.id())) {
            throw new NullValueInsertionException("The user ID is required for update");
        }

        existsUser(userRequestDTO.id());
        User user = UserMapper.toUser(userRequestDTO);
        user.setId(userRequestDTO.id());

        //TODO Encrypt user password here
        user.setPassword(userRequestDTO.password());

        return UserMapper.toResponse(userRepository.save(user));
    }

    public UserResponseDTO findById(UUID userId) {
        return UserMapper.toResponse(findUserById(userId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(UUID userId) {
        if (userId != null) {
            try {
                userRepository.deleteById(userId);
            } catch (EmptyResultDataAccessException e) {
                throw new EntityNotFoundException(Product.class);
            }
        } else {
            throw new NullValueInsertionException("ID is required for user removal");
        }
    }

    private User findUserById(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new NullValueInsertionException("ID is required for user search");
        }

        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class));
    }

    private void existsUser(UUID userId) {
        boolean exist = userRepository.existsById(userId);
        if (!exist) {
            throw new EntityNotFoundException(User.class);
        }
    }
}
