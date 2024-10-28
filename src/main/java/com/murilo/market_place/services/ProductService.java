package com.murilo.market_place.services;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductResponseDTO;
import com.murilo.market_place.exception.BucketS3InsertException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.exception.ObjectNotFoundException;
import com.murilo.market_place.mapper.ProductMapper;
import com.murilo.market_place.repositories.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final IProductRepository repository;
    private final S3Client s3Client;

    @Transactional(rollbackFor = Exception.class)
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = ProductMapper.toProduct(productRequestDTO);
        product.setThumb(uploadImg(productRequestDTO.thumb()));

        return ProductMapper.toResponse(repository.save(product));
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductResponseDTO updateProduct(UUID productId, ProductRequestDTO productRequestDTO) {
        existsProduct(productId);
        Product product = ProductMapper.toProduct(productRequestDTO);
        product.setId(productId);
        return ProductMapper.toResponse(repository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(UUID productId) {
        return ProductMapper.toResponse(findProduct(productId));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAllProducts(Pageable pageable) {
        Page<Product> productPage = repository.findAll(pageable);
        return new PageImpl<>(productPage.stream()
                .map(ProductMapper::toResponse).toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(UUID productId) {
        if (productId != null) {
            try {
                repository.deleteById(productId);
            } catch (EmptyResultDataAccessException e) {
                throw new ObjectNotFoundException(Product.class);
            }
        } else {
            throw new NullInsertValueException("ID is required for product removal");
        }
    }

    private String uploadImg(MultipartFile image) {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        try {
            PutObjectRequest putObjRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjRequest, RequestBody.fromByteBuffer(ByteBuffer.wrap(image.getBytes())));

            GetUrlRequest request = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            return s3Client.utilities().getUrl(request).toString();
        } catch (IOException e) {
            throw new BucketS3InsertException(e.getMessage());
        }
    }

    private Product findProduct(UUID productId) {
        return repository.findById(productId).orElseThrow(() -> new ObjectNotFoundException(Product.class));
    }

    private void existsProduct(UUID productId) {
        boolean exist = repository.existsById(productId);
        if (!exist) {
            throw new ObjectNotFoundException(Product.class);
        }
    }
}
