package com.murilo.market_place.controllers;

import com.murilo.market_place.controllers.documentation.IProductDocController;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductResponseDTO;
import com.murilo.market_place.mapper.ProductMapper;
import com.murilo.market_place.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController implements IProductDocController {

    private final ProductService service;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductResponseDTO> create(
            @ModelAttribute @Valid ProductRequestDTO productRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toResponse(service.createProduct(productRequestDTO)));
    }

    @PutMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductResponseDTO> update(
            @ModelAttribute @Valid ProductRequestDTO productRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductMapper.toResponse(service.updateProduct(productRequestDTO)));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> findById(
            @PathVariable("productId") UUID productId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ProductMapper.toResponse(service.findById(productId)));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new PageImpl<>(service.findAllProducts(PageRequest.of(page, size)).stream()
                .map(ProductMapper::toResponse).toList())
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(
            @PathVariable("productId") UUID productId
    ) {
        service.deleteById(productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
