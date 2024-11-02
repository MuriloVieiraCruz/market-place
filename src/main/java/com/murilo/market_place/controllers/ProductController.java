package com.murilo.market_place.controllers;

import com.murilo.market_place.controllers.documentation.IProductDocController;
import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductResponseDTO;
import com.murilo.market_place.dtos.product.ProductUpdateRequestDTO;
import com.murilo.market_place.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @PostMapping(value = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductResponseDTO> create(
            @ModelAttribute @Valid ProductRequestDTO productRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(productRequestDTO));
    }

    @PutMapping(value = "/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductResponseDTO> update(
            @ModelAttribute @Valid ProductUpdateRequestDTO productRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateProduct(productRequestDTO));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> findById(
            @PathVariable("productId") UUID productId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(productId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllProducts(PageRequest.of(page, size)));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> delete(
            @PathVariable("productId") UUID productId
    ) {
        service.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
