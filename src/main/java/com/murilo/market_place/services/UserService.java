package com.murilo.market_place.services;

import com.murilo.market_place.domains.User;
import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.mapper.UserMapper;
import com.murilo.market_place.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public User createUser(UserRequestDTO userRequestDTO) {
        User user = UserMapper.toUser(userRequestDTO);

        //TODO Encrypt user password here
        user.setPassword(userRequestDTO.getPassword());

        return userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public User updateUser(UserRequestDTO userRequestDTO) {
        if (Objects.isNull(userRequestDTO.getId())) {
            throw new NullInsertValueException("The user ID is required for update");
        }

        existsUser(userRequestDTO.getId());
        User user = UserMapper.toUser(userRequestDTO);
        user.setId(userRequestDTO.getId());

        //TODO Encrypt user password here
        user.setPassword(userRequestDTO.getPassword());

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

    private void existsUser(UUID userId) {
        if (userId != null) {
            boolean exist = userRepository.existsById(userId);
            if (!exist) {
                throw new EntityNotFoundException(User.class);
            }
        } else {
            throw new NullInsertValueException("ID is required for user removal");
        }
    }
}
