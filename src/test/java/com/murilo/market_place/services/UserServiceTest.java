package com.murilo.market_place.services;

import com.murilo.market_place.domains.User;
import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.dtos.user.UserResponseDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullValueInsertionException;
import com.murilo.market_place.factory.UserFactory;
import com.murilo.market_place.mapper.UserMapper;
import com.murilo.market_place.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private IUserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UUID> idCaptor;

    private UserRequestDTO userRequestDTO;
    private User user;

    @BeforeEach
    void setup() {
        userRequestDTO = UserFactory.getUserRequestInstance();
        user = UserFactory.getUserInstance();
    }

    @Nested
    class create {

        @Test
        void testCaseSuccessCreate() {
            when(userRepository.save(userCaptor.capture()))
                    .thenReturn(user);

            UserResponseDTO response = userService.createUser(userRequestDTO);

            assertNotNull(response);
            assertEquals(UserMapper.toResponse(user), response);
            assertEquals(UserMapper.toUser(userRequestDTO), userCaptor.getValue());

            verify(userRepository, atLeastOnce()).save(any());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void testCaseExceptionTriggersRollbackCreate() {
            when(userRepository.save(userCaptor.capture()))
                    .thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> userService.createUser(userRequestDTO));
            assertEquals(UserMapper.toUser(userRequestDTO), userCaptor.getValue());

            verify(userRepository).save(any());
        }
    }

    @Nested
    class update {

        @Test
        void testCaseSuccessUpdate() {
            userRequestDTO = UserFactory.getUserUpdateInstance();

            when(userRepository.existsById(userRequestDTO.id()))
                    .thenReturn(true);

            when(userRepository.save(userCaptor.capture()))
                    .thenReturn(user);

            UserResponseDTO response = userService.updateUser(userRequestDTO);

            var userRequest = UserMapper.toUser(userRequestDTO);
            userRequest.setId(userRequestDTO.id());

            assertNotNull(response);
            assertEquals(UserMapper.toResponse(user), response);
            assertEquals(userRequest, userCaptor.getValue());

            verify(userRepository, atLeastOnce()).save(any());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void testCaseNullIdUpdate() {
            assertThrows(NullValueInsertionException.class, () -> userService.updateUser(userRequestDTO));

            verify(userRepository, never()).existsById(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        void testCaseUserNotExistsUpdate() {
            userRequestDTO = UserFactory.getUserUpdateInstance();

            when(userRepository.existsById(any()))
                    .thenReturn(false);

            assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userRequestDTO));

            verify(userRepository, never()).save(any());
            verify(userRepository, atLeastOnce()).existsById(any());
        }

        @Test
        void testCaseExceptionTriggersRollbackUpdate() {
            assertThrows(RuntimeException.class, () -> userService.updateUser(userRequestDTO));

            verify(userRepository, never()).save(any());
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Nested
    class findById {

        @Test
        void testCaseSuccessFindById() {
            when(userRepository.findById(idCaptor.capture())).thenReturn(Optional.ofNullable(user));

            UserResponseDTO response = userService.findById(user.getId());

            assertNotNull(response);
            assertEquals(UserMapper.toResponse(user), response);
            assertEquals(user.getId(), idCaptor.getValue());

            verify(userRepository, atLeastOnce()).findById(any());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void testCaseNotFoundById() {
            when(userRepository.findById(user.getId())).thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> userService.findById(user.getId()));

            verify(userRepository, atLeastOnce()).findById(any());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void testCaseNullId() {
            assertThrows(NullValueInsertionException.class, () -> userService.findById(null));

            verify(userRepository, never()).findById(any());
        }
    }

    @Nested
    class delete {

        @Test
        void testCaseSuccessDelete() {
            doNothing().when(userRepository).deleteById(idCaptor.capture());

            userService.deleteUser(user.getId());

            assertEquals(user.getId(), idCaptor.getValue());

            verify(userRepository, atLeastOnce()).deleteById(any());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void testCaseNotFoundIdDelete() {
            doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(idCaptor.capture());

            assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(user.getId()));
            assertEquals(user.getId(), idCaptor.getValue());

            verify(userRepository, atLeastOnce()).deleteById(any());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void testCaseNullId() {
            assertThrows(NullValueInsertionException.class, () -> userService.deleteUser(null));

            verify(userRepository, never()).deleteById(any());
        }
    }
}
