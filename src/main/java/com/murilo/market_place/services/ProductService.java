package com.murilo.market_place.services;

import com.murilo.market_place.domains.Product;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductResponseDTO;
import com.murilo.market_place.exception.BucketS3InsertException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.exception.ObjectNotFoundException;
import com.murilo.market_place.mapper.IProductMapper;
import com.murilo.market_place.repositories.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductService {

    @Value("${aws.bucket.name}")
    private final String bucketName;

    private final IProductRepository repository;
    private final IProductMapper productMapper;
    private final S3Client s3Client;

    @Transactional(rollbackFor = Exception.class)
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = productMapper.toProduct(productRequestDTO);
        product.setThumb(uploadImg(productRequestDTO.thumb()));

        return productMapper.toResponse(repository.save(product));
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductResponseDTO updateProduct(UUID productId, ProductRequestDTO productRequestDTO) {
        existsProduct(productId);
        Product product = productMapper.toProduct(productRequestDTO);
        product.setId(productId);
        return productMapper.toResponse(repository.save(product));
    }


    @Transactional(readOnly = true)
    public ProductResponseDTO findById(UUID productId) {
        return productMapper.toResponse(findProduct(productId));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = repository.findAll(pageable);
        return productPage.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(UUID productId) {
        if (productId != null) {
            repository.deleteById(productId);
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
