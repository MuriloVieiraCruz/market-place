package com.murilo.market_place.services;

import com.murilo.market_place.domains.User;
import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullInsertValueException;
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

    private UUID existUserId;
    private UUID nonExistUserId;
    private UserRequestDTO userRequestDTO;
    private User user;

    @BeforeEach
    void setup() {
        userRequestDTO = UserFactory.getUserRequestInstance();
        user = UserFactory.getUserInstance();
        existUserId = user.getId();
        nonExistUserId = UUID.randomUUID();
    }

    @Nested
    class create {

        @Test
        void testCaseSuccessCreate() {
            when(userRepository.save(userCaptor.capture()))
                    .thenReturn(user);

            User response = userService.createUser(userRequestDTO);

            assertNotNull(response);
            assertEquals(user.getId(), response.getId());
            assertEquals(user.getName(), response.getName());
            assertEquals(user.getCpf(), response.getCpf());
            assertEquals(user.getEmail(), response.getEmail());
            assertEquals(user.getPassword(), response.getPassword());

            assertEquals(user.getName(), userCaptor.getValue().getName());
            assertEquals(user.getCpf(), userCaptor.getValue().getCpf());
            assertEquals(user.getPassword(), userCaptor.getValue().getPassword());

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
        void testCaseSuccess() {
            userRequestDTO.setId(existUserId);

            when(userRepository.existsById(userRequestDTO.getId()))
                    .thenReturn(true);

            when(userRepository.save(userCaptor.capture()))
                    .thenReturn(user);

            User response = userService.updateUser(userRequestDTO);

            var userRequest = UserMapper.toUser(userRequestDTO);
            userRequest.setId(userRequestDTO.getId());

            assertNotNull(response);
            assertEquals(user.getId(), response.getId());
            assertEquals(user.getName(), response.getName());
            assertEquals(user.getCpf(), response.getCpf());
            assertEquals(user.getEmail(), response.getEmail());
            assertEquals(user.getPassword(), response.getPassword());

            assertEquals(user.getName(), userCaptor.getValue().getName());
            assertEquals(user.getCpf(), userCaptor.getValue().getCpf());
            assertEquals(user.getPassword(), userCaptor.getValue().getPassword());

            verify(userRepository, atLeastOnce()).save(any());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void testCaseNullId() {
            assertThrows(NullInsertValueException.class, () -> userService.updateUser(userRequestDTO));

            verify(userRepository, never()).existsById(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        void testCaseUserNotExists() {
            userRequestDTO.setId(existUserId);

            when(userRepository.existsById(any()))
                    .thenReturn(false);

            assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userRequestDTO));

            verify(userRepository, never()).save(any());
            verify(userRepository, atLeastOnce()).existsById(any());
        }

        @Test
        void testCaseExceptionTriggersRollback() {
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

            User response = userService.findById(existUserId);

            assertNotNull(response);
            assertEquals(user.getId(), response.getId());
            assertEquals(user.getName(), response.getName());
            assertEquals(user.getCpf(), response.getCpf());
            assertEquals(user.getEmail(), response.getEmail());
            assertEquals(user.getPassword(), response.getPassword());
            assertEquals(existUserId, idCaptor.getValue());

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
            assertThrows(NullInsertValueException.class, () -> userService.findById(null));

            verify(userRepository, never()).findById(any());
        }
    }

    @Nested
    class deleteById {

        @Test
        void testCaseSuccess() {
            when(userRepository.existsById(idCaptor.capture())).thenReturn(true);
            doNothing().when(userRepository).deleteById(idCaptor.capture());

            userService.deleteById(user.getId());

            assertEquals(existUserId, idCaptor.getValue());

            verify(userRepository, atLeastOnce()).deleteById(any());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void testCaseNotFoundId() {
            doThrow(EntityNotFoundException.class).when(userRepository).existsById(idCaptor.capture());

            assertThrows(EntityNotFoundException.class, () -> userService.deleteById(nonExistUserId));
            assertEquals(nonExistUserId, idCaptor.getValue());

            verify(userRepository, atLeastOnce()).existsById(any());
            verify(userRepository, never()).deleteById(any());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        void testCaseNullId() {
            assertThrows(NullInsertValueException.class, () -> userService.deleteById(null));

            verify(userRepository, never()).existsById(any());
            verify(userRepository, never()).deleteById(any());
            verifyNoMoreInteractions(userRepository);
        }
    }
}
