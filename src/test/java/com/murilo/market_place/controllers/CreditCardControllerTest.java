package com.murilo.market_place.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.murilo.market_place.domains.CreditCard;
import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.dtos.creditCard.CreditCardResponseDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.factory.CreditCardFactory;
import com.murilo.market_place.services.CreditCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CreditCardController.class)
class CreditCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditCardService creditCardService;
    private final String baseUrl = "/api/v1/cards";

    @Autowired
    private ObjectMapper objectMapper;
    private CreditCard creditcard;
    private CreditCardRequestDTO requestDTO;
    private CreditCardResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        creditcard = CreditCardFactory.getCardInstance();
        requestDTO = CreditCardFactory.getCardRequestInstance();
    }

    @Nested
    class addCard {

        @Test
        void testCaseSuccess() throws Exception {
            when(creditCardService.create(any()))
                    .thenReturn(creditcard);

            mockMvc.perform(post(baseUrl + "/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isCreated());
        }

        @Test
        void testCaseUserNotFound() throws Exception {
            requestDTO = new CreditCardRequestDTO(null,
                    "1234567891011123",
                    "Usuario teste",
                    "555",
                    LocalDate.now().plusDays(1),
                    null);

            when(creditCardService.create(any()))
                    .thenThrow(NullInsertValueException.class);

            mockMvc.perform(post(baseUrl + "/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseBadRequest() throws Exception {
            when(creditCardService.create(any()))
                    .thenThrow(RuntimeException.class);

            mockMvc.perform(post(baseUrl + "/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(null)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class listAllCardsFromUser {

        @Test
        void testCaseSuccess() throws Exception {
            when(creditCardService.findAllCardsByUser(any()))
                    .thenReturn(List.of(creditcard));

            mockMvc.perform(get(baseUrl + "/search/" + requestDTO.userId()))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseUserNotFound() throws Exception {
            when(creditCardService.findAllCardsByUser(any()))
                    .thenThrow(NullInsertValueException.class);

            mockMvc.perform(get(baseUrl + "/search/" + null))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class findCardById {

        @Test
        void testCaseSuccess() throws Exception {
            when(creditCardService.findById(any()))
                    .thenReturn(creditcard);

            mockMvc.perform(get(baseUrl + "/" + creditcard.getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseNullValue() throws Exception {
            when(creditCardService.findById(any()))
                    .thenThrow(NullInsertValueException.class);

            mockMvc.perform(get(baseUrl + "/" + null))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseCardNotFound() throws Exception {
            when(creditCardService.findById(any()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(baseUrl + "/" + creditcard.getId()))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class deleteCardById {

        @Test
        void testCaseSuccess() throws Exception {
            doNothing().when(creditCardService).deleteById(any());

            mockMvc.perform(delete(baseUrl + "/delete/" + creditcard.getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseNullValue() throws Exception {
            doThrow(NullInsertValueException.class).when(creditCardService).deleteById(any());

            mockMvc.perform(delete(baseUrl + "/delete/" + null))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseNotFound() throws Exception {
            doThrow(EntityNotFoundException.class).when(creditCardService).deleteById(any());

            mockMvc.perform(delete(baseUrl + "/delete/" + creditcard.getId()))
                    .andExpect(status().isBadRequest());
        }
    }
}