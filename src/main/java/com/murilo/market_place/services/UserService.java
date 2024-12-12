package com.murilo.market_place.services;

import com.murilo.market_place.domains.User;
import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.mapper.UserMapper;
import com.murilo.market_place.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public User createUser(UserRequestDTO userRequestDTO) {
        User user = UserMapper.toUser(userRequestDTO);
        validateUser(user);
        String passwordEncoded = passwordEncoder.encode(userRequestDTO.getPassword());
        user.setPassword(passwordEncoded);

        return userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public User updateUser(UserRequestDTO userRequestDTO) {
        if (Objects.isNull(userRequestDTO.getId())) {
            throw new NullInsertValueException("The user ID is required for update");
        }

        existsUser(userRequestDTO.getId());
        User user = UserMapper.toUser(userRequestDTO);
        validateUser(user);
        user.setId(userRequestDTO.getId());

        String passwordEncoded = passwordEncoder.encode(userRequestDTO.getPassword());
        user.setPassword(passwordEncoded);

        return userRepository.save(user);
    }

    public User findById(UUID userId) {
        return findUserById(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(UUID userId) {
        existsUser(userId);
        userRepository.deleteById(userId);
    }

    private User findUserById(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new NullInsertValueException("ID is required for user search");
        }

        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class));
    }

    public void existsUser(UUID userId) {
        if (userId != null) {
            boolean exist = userRepository.existsById(userId);
            if (!exist) {
                throw new EntityNotFoundException(User.class);
            }
        } else {
            throw new NullInsertValueException("ID is required for user removal");
        }
    }

    private void validateUser(User user) {
        if (user.getId() != null) {
            validateCpfAndEmailForExistingUser(user);
        } else {
            validateCpfAndEmailForNewUser(user);
        }
    }

    private void validateCpfAndEmailForExistingUser(User user) {
        userRepository.findByCpf(user.getCpf())
                .filter(existingUser -> !existingUser.getId().equals(user.getId()))
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("CPF is already in use");
                });

        userRepository.findByEmail(user.getEmail())
                .filter(existingUser -> !existingUser.getId().equals(user.getId()))
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("Email is already in use");
                });
    }

    private void validateCpfAndEmailForNewUser(User user) {
        if (userRepository.existsByCpf(user.getCpf())) {
            throw new IllegalArgumentException("CPF is already in use.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
    }
}
