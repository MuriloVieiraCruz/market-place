package com.murilo.market_place.controllers;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductResponseDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.factory.ProductFactory;
import com.murilo.market_place.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    private final String baseUrl = "/api/v1/products";

    private Product product;
    private ProductRequestDTO productRequestDTO;
    private ProductResponseDTO productResponseDTO;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    void setup() {
        product = ProductFactory.getProductInstance();
        productRequestDTO = ProductFactory.getProductRequestInstance();
        productResponseDTO = ProductFactory.getProductResponseInstance();
        productRequestDTO = ProductFactory.getProductUpdateRequestInstance();
    }

    @Test
    void testCaseSuccessCreation() throws Exception {
        when(productService.createProduct(productRequestDTO))
                .thenReturn(productResponseDTO);

        mockMvc.perform(multipart(baseUrl + "/create")
                .file("thumb", productRequestDTO.thumb().getBytes())
                .param("artist", productRequestDTO.artist())
                .param("year", productRequestDTO.year().toString())
                .param("album", productRequestDTO.album())
                .param("price", productRequestDTO.price().toString())
                .param("store", productRequestDTO.store())
                .param("date", productRequestDTO.date().toString())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
        .andExpect(status().isCreated());
    }

    @Test
    void testBadRequestCreation() throws Exception {
        when(productService.createProduct(null)).thenThrow(new NullPointerException());
        mockMvc.perform(multipart(baseUrl + "/create")).andExpect(status().isBadRequest());
    }

    @Test
    void testIOExceptionCreation() throws Exception {
        when(productService.createProduct(null)).thenThrow(new IOException());
        mockMvc.perform(multipart(baseUrl + "/create")).andExpect(status().isBadRequest());
    }

    @Test
    void testSuccessUpdate() throws Exception {
        when(productService.updateProduct(productRequestDTO))
                .thenReturn(productResponseDTO);

        MockMultipartHttpServletRequestBuilder multipart = (MockMultipartHttpServletRequestBuilder) multipart(baseUrl + "/update/" + product.getId()).with(request -> {
            request.setMethod(String.valueOf(HttpMethod.PUT));
            return request;
        });

        mockMvc.perform(multipart
                .file("thumb", productRequestDTO.thumb().getBytes())
                .param("artist", productRequestDTO.artist())
                .param("year", productRequestDTO.year().toString())
                .param("album", productRequestDTO.album())
                .param("price", productRequestDTO.price().toString())
                .param("store", productRequestDTO.store())
                .param("date", productRequestDTO.date().toString())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andExpect(status().isOk());
    }

    @Test
    void testBadRequestUpdate() throws Exception {
        when(productService.updateProduct(productRequestDTO))
                .thenThrow(new EntityNotFoundException(Product.class));

        MockMultipartHttpServletRequestBuilder multipart = (MockMultipartHttpServletRequestBuilder) multipart(baseUrl + "/update/").with(request -> {
            request.setMethod(String.valueOf(HttpMethod.PUT));
            return request;
        });

        mockMvc.perform(multipart
                .file("thumb", productRequestDTO.thumb().getBytes())
                .param("artist", productRequestDTO.artist())
                .param("year", productRequestDTO.year().toString())
                .param("album", productRequestDTO.album())
                .param("price", productRequestDTO.price().toString())
                .param("store", productRequestDTO.store())
                .param("date", productRequestDTO.date().toString())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andExpect(status().isNotFound());
    }

    @Test
    void testSuccessFindById() throws Exception {
        when(productService.findById(product.getId())).thenReturn(productResponseDTO);

        mockMvc.perform(get(baseUrl + "/" + product.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testNotFoundFindById() throws Exception {
        when(productService.findById(product.getId())).thenReturn(null);

        mockMvc.perform(get(baseUrl + "/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSuccessFindAll() throws Exception {
        when(productService.findAllProducts(PageRequest.of(0, 1))).thenReturn(new PageImpl<>(List.of(productResponseDTO)));

        mockMvc.perform(get(baseUrl + "/search"))
                .andExpect(status().isOk());
    }

    @Test
    void testSuccessDelete() throws Exception {
        doNothing().when(productService).deleteProduct(product.getId());

        mockMvc.perform(delete(baseUrl + "/delete/" + product.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testNotFoundDelete() throws Exception {
        doThrow(new EntityNotFoundException(Product.class)).when(productService).deleteProduct(product.getId());

        mockMvc.perform(delete(baseUrl + "/delete/" + null))
                .andExpect(status().isBadRequest());
    }
}