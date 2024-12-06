package com.murilo.market_place.controllers;

import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.factory.ProductFactory;
import com.murilo.market_place.repositories.IProductRepository;
import com.murilo.market_place.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private S3Client s3Client;

    @Autowired
    private ProductService productService;

    @Autowired
    private IProductRepository productRepository;

    private final String baseUrl = "/api/v1/products";

    private UUID existProductId;
    private UUID nonExitedProductId;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    void setup() throws MalformedURLException {
        S3Utilities s3Utilities = mock(S3Utilities.class);
        when(s3Client.utilities()).thenReturn(s3Utilities);
        URL mockUrl = URI.create("https://mock-bucket.s3.amazonaws.com/test-image.jpg").toURL();
        GetUrlRequest anyGetUrlRequest = any(GetUrlRequest.class);
        when(s3Utilities.getUrl(anyGetUrlRequest)).thenReturn(mockUrl);
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        productRepository.deleteAll();
        productRequestDTO = ProductFactory.getProductRequestInstance();
        existProductId = productService.createProduct(productRequestDTO).getId();
        nonExitedProductId = UUID.randomUUID();
    }

    @Nested
    class create {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(multipart(baseUrl + "/create")
                            .file("thumb", productRequestDTO.getThumb().get().getBytes())
                            .param("artist", productRequestDTO.getArtist())
                            .param("year", productRequestDTO.getYear().toString())
                            .param("album", productRequestDTO.getAlbum())
                            .param("price", productRequestDTO.getPrice().toString())
                            .param("store", productRequestDTO.getStore())
                            .param("date", productRequestDTO.getDate().toString())
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.thumb").exists())
                    .andExpect(jsonPath("$.artist").value(productRequestDTO.getArtist()))
                    .andExpect(jsonPath("$.year").value(productRequestDTO.getYear()))
                    .andExpect(jsonPath("$.album").value(productRequestDTO.getAlbum()))
                    .andExpect(jsonPath("$.price").value(productRequestDTO.getPrice()))
                    .andExpect(jsonPath("$.store").value(productRequestDTO.getStore()))
                    .andExpect(jsonPath("$.date").value(productRequestDTO.getDate().toString()));
        }

        @Test
        void testCaseBadRequest() throws Exception {
            mockMvc.perform(multipart(baseUrl + "/create")).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class update {

        @Test
        void testCaseSuccess() throws Exception {
            productRequestDTO.setId(existProductId);

            MockMultipartHttpServletRequestBuilder multipart = (MockMultipartHttpServletRequestBuilder) multipart(baseUrl + "/update").with(request -> {
                request.setMethod(String.valueOf(HttpMethod.PUT));
                return request;
            });

            mockMvc.perform(multipart
                    .file("thumb", productRequestDTO.getThumb().get().getBytes())
                    .param("id", productRequestDTO.getId().toString())
                    .param("artist", productRequestDTO.getArtist())
                    .param("year", productRequestDTO.getYear().toString())
                    .param("album", productRequestDTO.getAlbum())
                    .param("price", productRequestDTO.getPrice().toString())
                    .param("store", productRequestDTO.getStore())
                    .param("date", productRequestDTO.getDate().toString())
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseBadRequest() throws Exception {
            MockMultipartHttpServletRequestBuilder multipart = (MockMultipartHttpServletRequestBuilder) multipart(baseUrl + "/update/").with(request -> {
                request.setMethod(String.valueOf(HttpMethod.PUT));
                return request;
            });

            mockMvc.perform(multipart
                    .file("thumb", productRequestDTO.getThumb().get().getBytes())
                    .param("artist", productRequestDTO.getArtist())
                    .param("year", productRequestDTO.getYear().toString())
                    .param("album", productRequestDTO.getAlbum())
                    .param("price", productRequestDTO.getPrice().toString())
                    .param("store", productRequestDTO.getStore())
                    .param("date", productRequestDTO.getDate().toString())
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)).andExpect(status().isNotFound());
        }

        @Test
        void testCaseNullInsertValueException() throws Exception {
            MockMultipartHttpServletRequestBuilder multipart = (MockMultipartHttpServletRequestBuilder) multipart(baseUrl + "/update/").with(request -> {
                request.setMethod(String.valueOf(HttpMethod.PUT));
                return request;
            });

            mockMvc.perform(multipart
                    .file("thumb", productRequestDTO.getThumb().get().getBytes())
                    .param("artist", productRequestDTO.getArtist())
                    .param("year", productRequestDTO.getYear().toString())
                    .param("album", productRequestDTO.getAlbum())
                    .param("price", productRequestDTO.getPrice().toString())
                    .param("store", productRequestDTO.getStore())
                    .param("date", productRequestDTO.getDate().toString())
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));
        }
    }

    @Nested
    class findById {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + existProductId))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseNullId() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + null))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseNotFound() throws Exception {
            mockMvc.perform(get(baseUrl + "/" + nonExitedProductId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
        }
    }

    @Nested
    class findAll {

        @Test
        void testSuccessFindAll() throws Exception {
            mockMvc.perform(get(baseUrl + "/search"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class deleteById {

        @Test
        void testCaseSuccess() throws Exception {
            mockMvc.perform(delete(baseUrl + "/delete/" + existProductId))
                    .andExpect(status().isOk());
        }

        @Test
        void testCaseNullId() throws Exception {
            mockMvc.perform(delete(baseUrl + "/delete/" + null))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testCaseNotFound() throws Exception {
            mockMvc.perform(delete(baseUrl + "/delete/" + nonExitedProductId))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
        }
    }
}