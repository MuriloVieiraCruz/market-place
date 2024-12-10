package com.murilo.market_place.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.transaction.TransactionRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.factory.ProductFactory;
import com.murilo.market_place.factory.TransactionFactory;
import com.murilo.market_place.factory.UserFactory;
import com.murilo.market_place.repositories.ITransactionRepository;
import com.murilo.market_place.services.ProductService;
import com.murilo.market_place.services.TransactionService;
import com.murilo.market_place.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ITransactionRepository transactionRepository;

    @MockBean
    private S3Client s3Client;

    private final String baseUrl = "/api/v1/transactions";

    private UUID existTransactionId;
    private UUID nonExistTransactionId;
    private UUID existUserId;
    private UUID nonExistedUserId;
    private Product product;
    private TransactionRequestDTO transactionRequestDTO;

    @BeforeEach
    void setup() throws MalformedURLException {
        S3Utilities s3Utilities = mock(S3Utilities.class);
        when(s3Client.utilities()).thenReturn(s3Utilities);
        URL mockUrl = URI.create("https://mock-bucket.s3.amazonaws.com/test-image.jpg").toURL();
        GetUrlRequest anyGetUrlRequest = any(GetUrlRequest.class);
        when(s3Utilities.getUrl(anyGetUrlRequest)).thenReturn(mockUrl);
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        transactionRepository.deleteAll();
        existUserId = userService.createUser(UserFactory.getUserRequestInstance()).getId();
        product = productService.createProduct(ProductFactory.getProductRequestInstance());
        transactionRequestDTO = TransactionFactory.getTransactionRequestInstance(existUserId, product);
        existTransactionId = transactionService.create(transactionRequestDTO).getId();
        nonExistTransactionId = UUID.randomUUID();
        nonExistedUserId = UUID.randomUUID();
    }

    @Nested
    class create {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(post(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionRequestDTO)))
                    .andExpect(status().isCreated());
//                    .andExpect(jsonPath("$.id").exists())
//                    .andExpect(jsonPath("$.number").value(requestDTO.getNumber()))
//                    .andExpect(jsonPath("$.holderName").value(requestDTO.getHolderName()))
//                    .andExpect(jsonPath("$.cvv").value(requestDTO.getCvv()))
//                    .andExpect(jsonPath("$.expirationDate").value(requestDTO.getExpirationDate().toString()))
//                    .andExpect(jsonPath("$.userId").value(requestDTO.getUserId().toString()));
        }

        @Test
        void testCaseUserNotFound() throws Exception {
            transactionRequestDTO.setUserId(null);

            mockMvc.perform(post(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionRequestDTO)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseProductNotFound() throws Exception {
            transactionRequestDTO.getPurchaseProducts().getFirst().setProductId(null);

            mockMvc.perform(post(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionRequestDTO)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseBadRequest() throws Exception {
            mockMvc.perform(post(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class findAll {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(get(baseUrl + "/search/" + existUserId))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseNullUserId() throws Exception {
            mockMvc.perform(get(baseUrl + "/search/" + null))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseUserNotFound() throws Exception {
            mockMvc.perform(get(baseUrl + "/search/" + nonExistedUserId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
        }
    }

    @Nested
    class findCardById {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + existTransactionId))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseNullValue() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + null))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseCardNotFound() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + nonExistTransactionId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
        }
    }
}
