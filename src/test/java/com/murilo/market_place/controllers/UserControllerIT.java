package com.murilo.market_place.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.factory.UserFactory;
import com.murilo.market_place.repositories.IUserRepository;
import com.murilo.market_place.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserService userService;

    private final String baseUrl = "/api/v1/users";

    private UUID existUserId;
    private UUID nonExitedUserId;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        userRequestDTO = UserFactory.getUserRequestInstance();
        existUserId = userService.createUser(userRequestDTO).getId();
        nonExitedUserId = UUID.randomUUID();
    }

    @Nested
    class create {

        @Test
        void testCaseSuccess() throws Exception {
            userRequestDTO.setEmail("segundoTest@gmail.com");

            mockMvc.perform(post(baseUrl + "/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value(userRequestDTO.getName()))
                    .andExpect(jsonPath("$.cpf").value(userRequestDTO.getCpf()))
                    .andExpect(jsonPath("$.email").value(userRequestDTO.getEmail()))
                    .andExpect(jsonPath("$.password").value(userRequestDTO.getPassword()));

            assertFalse(userRepository.findAll().isEmpty(), "User was not persisted in the database");
        }

        @Test
        void testCaseBadRequest() throws Exception {
            mockMvc.perform(post(baseUrl + "/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class update {

        @Test
        void testCaseSuccess() throws Exception {
            userRequestDTO.setId(existUserId);

            mockMvc.perform(put(baseUrl + "/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value(userRequestDTO.getName()))
                    .andExpect(jsonPath("$.cpf").value(userRequestDTO.getCpf()))
                    .andExpect(jsonPath("$.email").value(userRequestDTO.getEmail()))
                    .andExpect(jsonPath("$.password").value(userRequestDTO.getPassword()));
        }

        @Test
        void testCaseBadRequest() throws Exception {
            mockMvc.perform(put(baseUrl + "/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseNullInsertValueException() throws Exception {
            mockMvc.perform(put(baseUrl + "/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertInstanceOf(NullInsertValueException.class, result.getResolvedException()));
        }
    }

    @Nested
    class findById {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + existUserId))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseNullId() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + null))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseNotFound() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + nonExitedUserId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
        }
    }

    @Nested
    class deleteById {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(delete(baseUrl + "/delete/" + existUserId))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseNullId() throws Exception {
            mockMvc.perform(delete(baseUrl + "/delete/" + null))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseNotFound() throws Exception {
            mockMvc.perform(delete(baseUrl + "/delete/" + nonExitedUserId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
        }
    }
}