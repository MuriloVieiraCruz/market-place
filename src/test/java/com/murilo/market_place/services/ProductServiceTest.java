package com.murilo.market_place.services;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductResponseDTO;
import com.murilo.market_place.exception.BucketS3InsertException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.exception.ObjectNotFoundException;
import com.murilo.market_place.factory.ProductFactory;
import com.murilo.market_place.repositories.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private ProductRequestDTO productRequestDTO;
    private Product product;
    private UUID existingId;

    private static final String BUCKET_NAME = "test-bucket";

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(productService, "bucketName", BUCKET_NAME);
        product = ProductFactory.getProductInstance();
        productRequestDTO = ProductFactory.getProductRequestInstance();
        existingId = UUID.randomUUID();
    }

    @Test
    void testCaseCreateSuccess() {
        doAnswer(invocation -> null)
                .when(s3Client)
                .putObject(any(PutObjectRequest.class), any(RequestBody.class));

        S3Utilities s3UtilitiesMock = mock(S3Utilities.class);
        when(s3Client.utilities()).thenReturn(s3UtilitiesMock);

        when(s3UtilitiesMock.getUrl(any(GetUrlRequest.class)))
                .thenAnswer(invocation -> new URI("https://s3.amazonaws.com/" + BUCKET_NAME + "/darkSide.jpg").toURL());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO response = productService.createProduct(productRequestDTO);

        assertThat(response).isNotNull();
        assertEquals("Pink Floyd", response.artist());
        assertEquals(1973, response.year());
        assertEquals("Dask Side of The Moon", response.album());
        assertEquals(BigDecimal.valueOf(61.90), response.price());
        assertEquals("Vinil Records", response.store());
        assertEquals(LocalDate.now(), response.date());

        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        verify(s3Client).utilities();
        verify(s3UtilitiesMock).getUrl(any(GetUrlRequest.class));
    }

    @Test
    void testCase2CreateProductErrorGenerateImageURL() {
        doThrow(new IOException("Error uploading file"))
                .when(s3Client)
                .putObject(any(PutObjectRequest.class), any(RequestBody.class));

        assertThrows(BucketS3InsertException.class, () -> {
            productService.createProduct(productRequestDTO);
        });

        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testCaseUpdateSuccess() {
        when(productRepository.existsById(existingId)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO response = productService.updateProduct(existingId, productRequestDTO);

        assertThat(response).isNotNull();
        assertEquals("Pink Floyd", response.artist());
        assertEquals(1973, response.year());
        assertEquals("Dask Side of The Moon", response.album());
        assertEquals(BigDecimal.valueOf(61.90), response.price());
        assertEquals("Vinil Records", response.store());
        assertEquals(LocalDate.now(), response.date());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCaseUpdateNotFoundId() {
        when(productRepository.existsById(existingId)).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> productService.updateProduct(existingId, productRequestDTO));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testCaseFindByIdSuccess() {
        when(productRepository.findById(existingId)).thenReturn(Optional.ofNullable(product));

        ProductResponseDTO response = productService.findById(existingId);

        assertThat(response).isNotNull();
        assertEquals("Pink Floyd", response.artist());
        assertEquals(1973, response.year());
        assertEquals("Dask Side of The Moon", response.album());
        assertEquals(BigDecimal.valueOf(61.90), response.price());
        assertEquals("Vinil Records", response.store());
        assertEquals(LocalDate.now(), response.date());
    }

    @Test
    void testCaseFindByIdNotFound() {
        var nonExistingId = UUID.randomUUID();

        when(productRepository.findById(nonExistingId)).thenThrow(new ObjectNotFoundException(Product.class));
        assertThrows(ObjectNotFoundException.class, () -> productService.findById(nonExistingId));
    }

    @Test
    void findAllProducts() {
        when(productRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(product)));

        Page<ProductResponseDTO> response = productService.findAllProducts(PageRequest.of(0, 1));

        assertThat(response).isNotNull();
        assertEquals("Pink Floyd", response.getContent().getFirst().artist());
        assertEquals(1973, response.getContent().getFirst().year());
        assertEquals("Dask Side of The Moon", response.getContent().getFirst().album());
        assertEquals(BigDecimal.valueOf(61.90), response.getContent().getFirst().price());
        assertEquals("Vinil Records", response.getContent().getFirst().store());
        assertEquals(LocalDate.now(), response.getContent().getFirst().date());
    }

    @Test
    void testCaseDeleteProductSuccess() {
        doNothing().when(productRepository).deleteById(existingId);

        productService.deleteProduct(existingId);
        verify(productRepository, times(1)).deleteById(existingId);
    }

    @Test
    void testCaseDeleteProductNotFoundId() {
        var nonExistingId = UUID.randomUUID();

        doThrow(new EmptyResultDataAccessException(1)).when(productRepository).deleteById(nonExistingId);
        assertThrows(ObjectNotFoundException.class, () -> productService.deleteProduct(nonExistingId));
    }

    @Test
    void testCaseDeleteProductWithNoPassId() {
        assertThrows(NullInsertValueException.class, () -> productService.deleteProduct(null));
    }
}