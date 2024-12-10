package com.murilo.market_place.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.factory.CreditCardFactory;
import com.murilo.market_place.factory.UserFactory;
import com.murilo.market_place.repositories.ICreditCardRepository;
import com.murilo.market_place.services.CreditCardService;
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

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CreditCardControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UserService userService;

    @Autowired
    private ICreditCardRepository creditCardRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/v1/cards";

    private UUID existCreditCardId;
    private UUID userId;
    private UUID nonExitedCreditCardId;
    private CreditCardRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        creditCardRepository.deleteAll();
        objectMapper.registerModule(new JavaTimeModule());
        userId = userService.createUser(UserFactory.getUserRequestInstance()).getId();
        requestDTO = CreditCardFactory.getCardRequestInstance(userId);
        existCreditCardId = creditCardService.create(requestDTO).getId();
        nonExitedCreditCardId = UUID.randomUUID();
    }

    @Nested
    class create {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(post(baseUrl + "/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.number").value(requestDTO.getNumber()))
                    .andExpect(jsonPath("$.holderName").value(requestDTO.getHolderName()))
                    .andExpect(jsonPath("$.cvv").value(requestDTO.getCvv()))
                    .andExpect(jsonPath("$.expirationDate").value(requestDTO.getExpirationDate().toString()))
                    .andExpect(jsonPath("$.userId").value(requestDTO.getUserId().toString()));
        }

        @Test
        void testCaseUserNotFound() throws Exception {
            requestDTO.setUserId(null);

            mockMvc.perform(post(baseUrl + "/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseBadRequest() throws Exception {
            mockMvc.perform(post(baseUrl + "/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class listAllCardsFromUser {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(get(baseUrl + "/search/" + userId))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseUserNotFound() throws Exception {
            mockMvc.perform(get(baseUrl + "/search/" + null))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class findCardById {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + existCreditCardId))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseNullValue() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + null))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseCardNotFound() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + nonExitedCreditCardId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
        }
    }

    @Nested
    class deleteById {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(delete(baseUrl + "/" + existCreditCardId))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseNullId() throws Exception {
            mockMvc.perform(delete(baseUrl + "/" + null))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseNotFound() throws Exception {
            mockMvc.perform(delete(baseUrl + "/" + nonExitedCreditCardId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
        }
    }
}