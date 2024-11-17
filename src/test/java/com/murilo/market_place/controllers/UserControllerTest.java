package com.murilo.market_place.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.dtos.user.UserResponseDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullValueInsertionException;
import com.murilo.market_place.factory.UserFactory;
import com.murilo.market_place.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    private final String baseUrl = "/api/v1/users";
    private final UUID userId = UUID.randomUUID();

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setup() {
        userRequestDTO = UserFactory.getUserRequestInstance();
        userResponseDTO = UserFactory.getUserResponseInstance();
    }

    @Test
    void testCaseSuccessCreation() throws Exception {
        when(userService.createUser(userRequestDTO))
                .thenReturn(userResponseDTO);

        mockMvc.perform(post(baseUrl + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponseDTO)));
    }

    @Test
    void testCaseBadRequestCreation() throws Exception {
        when(userService.createUser(null))
                .thenThrow(new NullPointerException());

        mockMvc.perform(post(baseUrl + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCaseSuccessUpdate() throws Exception {
        userRequestDTO = UserFactory.getUserUpdateInstance();

        when(userService.updateUser(userRequestDTO))
                .thenReturn(userResponseDTO);

        mockMvc.perform(put(baseUrl + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponseDTO)));
    }

    @Test
    void testCaseBadRequestUpdate() throws Exception {
        when(userService.updateUser(null))
                .thenThrow(new NullPointerException());

        mockMvc.perform(put(baseUrl + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCaseNullInsertValueExceptionUpdate() throws Exception {
        when(userService.updateUser(userRequestDTO))
                .thenThrow(new NullValueInsertionException());

        mockMvc.perform(put(baseUrl + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(NullValueInsertionException.class, result.getResolvedException()));
    }

    @Test
    void testCaseSuccessFindById() throws Exception {
        when(userService.findById(userId))
                .thenReturn(userResponseDTO);

        mockMvc.perform(get(baseUrl + "/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    void testCaseNullIdFindById() throws Exception {
        when(userService.findById(null))
                .thenThrow(new NullValueInsertionException());

        mockMvc.perform(get(baseUrl + "/" + null))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCaseNotFoundFindById() throws Exception {
        when(userService.findById(userId))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(get(baseUrl + "/" + userId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
    }

    @Test
    void testCaseDeleteSuccessDelete() throws Exception {
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete(baseUrl + "/delete/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    void testCaseNullIdDelete() throws Exception {
        doThrow(new NullValueInsertionException()).when(userService).deleteUser(null);

        mockMvc.perform(delete(baseUrl + "/delete/" + null))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCaseNotFoundDelete() throws Exception {
        doThrow(new EntityNotFoundException()).when(userService).deleteUser(userId);

        mockMvc.perform(delete(baseUrl + "/delete/" + userId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
    }
}