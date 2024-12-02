package com.murilo.market_place.services;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.exception.BucketS3InsertException;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.factory.ProductFactory;
import com.murilo.market_place.mapper.ProductMapper;
import com.murilo.market_place.repositories.IProductRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private IProductRepository productRepository;

    @Mock
    private S3Client s3Client;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Captor
    private ArgumentCaptor<UUID> idCaptor;

    private ProductRequestDTO productRequestDTO;
    private Product product;

    private static final String BUCKET_NAME = "test-bucket";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(productService, "bucketName", BUCKET_NAME);
        product = ProductFactory.getProductInstance();
        productRequestDTO = ProductFactory.getProductRequestInstance();
    }

    @Nested
    class create {

        @Test
        void testCaseSuccessCreate() {
            doAnswer(invocation -> null)
                    .when(s3Client)
                    .putObject(any(PutObjectRequest.class), any(RequestBody.class));

            S3Utilities s3UtilitiesMock = mock(S3Utilities.class);
            when(s3Client.utilities()).thenReturn(s3UtilitiesMock);

            when(s3UtilitiesMock.getUrl(any(GetUrlRequest.class)))
                    .thenAnswer(invocation -> new URI("https://s3.amazonaws.com/" + BUCKET_NAME + "/darkSide.jpg").toURL());
            when(productRepository.save(productCaptor.capture())).thenReturn(product);

            Product response = productService.createProduct(productRequestDTO);

            assertNotNull(response);
            assertEquals("Pink Floyd", response.getArtist());
            assertEquals(1973, response.getYear());
            assertEquals("Dask Side of The Moon", response.getAlbum());
            assertEquals(BigDecimal.valueOf(61.90), response.getPrice());
            assertEquals("Vinil Records", response.getStore());
            assertEquals(LocalDate.now(), response.getDate());
            assertEquals(ProductMapper.toProduct(productRequestDTO), productCaptor.getValue());

            verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
            verify(s3Client).utilities();
            verify(s3UtilitiesMock).getUrl(any(GetUrlRequest.class));
            verify(productRepository, atLeastOnce()).save(any());
            verifyNoMoreInteractions(productRepository);
        }

        @Test
        void testCaseProductErrorGenerateImageURLCreate() {
            doThrow(IOException.class)
                    .when(s3Client)
                    .putObject(any(PutObjectRequest.class), any(RequestBody.class));

            assertThrows(BucketS3InsertException.class, () ->
                productService.createProduct(productRequestDTO));

            verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
            verify(productRepository, never()).save(any());
        }

    }

    @Nested
    class update {

        @Test
        void testCaseSuccessUpdate() {
            when(productRepository.existsById(idCaptor.capture())).thenReturn(true);
            when(productRepository.save(productCaptor.capture())).thenReturn(product);

            Product response = productService.updateProduct(productRequestDTO);

            assertNotNull(response);
            assertEquals("Pink Floyd", response.getArtist());
            assertEquals(1973, response.getYear());
            assertEquals("Dask Side of The Moon", response.getAlbum());
            assertEquals(BigDecimal.valueOf(61.90), response.getPrice());
            assertEquals("Vinil Records", response.getStore());
            assertEquals(LocalDate.now(), response.getDate());
            assertEquals(productRequestDTO.id(), idCaptor.getValue());

            verify(productRepository, atLeastOnce()).save(any());
            verifyNoMoreInteractions(productRepository);
        }

        @Test
        void testCaseNotFoundIdUpdate() {
            when(productRepository.findById(idCaptor.capture())).thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(productRequestDTO));
            assertEquals(productRequestDTO.id(), idCaptor.getValue());

            verify(productRepository, never()).save(any());
            verifyNoMoreInteractions(productRepository);
        }

        @Test
        void testCaseNullIdUpdate() {
            assertThrows(NullInsertValueException.class, () -> productService.updateProduct(productRequestDTO));
            assertEquals(productRequestDTO.id(), idCaptor.getValue());

            verify(productRepository, never()).save(any());
            verifyNoMoreInteractions(productRepository);
        }
    }

    @Nested
    class findById {

        @Test
        void testCaseSuccessFindById() {
            when(productRepository.findById(idCaptor.capture())).thenReturn(Optional.of(product));

            Product response = productService.findById(product.getId());

            assertNotNull(response);
            assertEquals("Pink Floyd", response.getArtist());
            assertEquals(1973, response.getYear());
            assertEquals("Dask Side of The Moon", response.getAlbum());
            assertEquals(BigDecimal.valueOf(61.90), response.getPrice());
            assertEquals("Vinil Records", response.getStore());
            assertEquals(LocalDate.now(), response.getDate());

            verify(productRepository, atLeastOnce()).findById(any());
            verifyNoMoreInteractions(productRepository);
        }

        @Test
        void testCaseNotFoundFindById() {
            when(productRepository.findById(idCaptor.capture())).thenThrow(new EntityNotFoundException(Product.class));

            assertThrows(EntityNotFoundException.class, () -> productService.findById(product.getId()));
            assertEquals(product.getId(), idCaptor.getValue());

            verify(productRepository, atLeastOnce()).findById(any());
            verifyNoMoreInteractions(productRepository);
        }

        @Test
        void testCaseNullIdFindById() {
            when(productRepository.findById(idCaptor.capture())).thenThrow(NullInsertValueException.class);

            assertThrows(NullInsertValueException.class, () -> productService.findById(null));
            assertEquals(product.getId(), idCaptor.getValue());

            verify(productRepository, never()).findById(any());
        }
    }

    @Nested
    class findAll {

        @Test
        void testCaseSuccessFindAll() {
            when(productRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(product)));

            Page<Product> response = productService.findAllProducts(PageRequest.of(0, 1));

            assertNotNull(response);
            assertEquals("Pink Floyd", response.getContent().getFirst().getArtist());
            assertEquals(1973, response.getContent().getFirst().getYear());
            assertEquals("Dask Side of The Moon", response.getContent().getFirst().getAlbum());
            assertEquals(BigDecimal.valueOf(61.90), response.getContent().getFirst().getPrice());
            assertEquals("Vinil Records", response.getContent().getFirst().getStore());
            assertEquals(LocalDate.now(), response.getContent().getFirst().getDate());

            verify(productRepository, atLeastOnce()).findAll(any(Pageable.class));
            verifyNoMoreInteractions(productRepository);
        }
    }

    @Nested
    class delete {

        @Test
        void testCaseSuccessDelete() {
            doNothing().when(productRepository).deleteById(idCaptor.capture());

            productService.deleteById(product.getId());

            assertEquals(product.getId(), idCaptor.getValue());

            verify(productRepository, atLeastOnce()).deleteById(any());
            verifyNoMoreInteractions(productRepository);
        }

        @Test
        void testCaseNotFoundIdDelete() {
            doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(idCaptor.capture());

            assertThrows(EntityNotFoundException.class, () -> productService.deleteById(product.getId()));
            assertEquals(product.getId(), idCaptor.getValue());

            verify(productRepository, atLeastOnce()).deleteById(any());
            verifyNoMoreInteractions(productRepository);
        }

        @Test
        void testCaseNullIdDelete() {
            assertThrows(NullInsertValueException.class, () -> productService.deleteById(null));

            verify(productRepository, never()).deleteById(any());
            verifyNoMoreInteractions(productRepository);
        }
    }
}