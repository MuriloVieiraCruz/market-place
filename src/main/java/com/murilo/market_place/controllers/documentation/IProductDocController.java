package com.murilo.market_place.controllers.documentation;

import com.murilo.market_place.dtos.product.ProductRequestDTO;
import com.murilo.market_place.dtos.product.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Product Management", description = "Operations related to managing products such as creation, updates, searches and deletion")
public interface IProductDocController {

    @Operation(
            description = "Register product",
            summary = "Responsible for registering an product, needs an product image of no more than 20 mg",
            responses = {
                    @ApiResponse(description = "Product registered", responseCode = "201"),
                    @ApiResponse(
                            description = "When the request is sent without the body or with some attribute missing",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"error\": \"The request body has some errors or doesn't exist\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "When the attribute has an invalid format",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"errors\": \"code: 008 message: error message\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<ProductResponseDTO> create(ProductRequestDTO productRequestDTO);

    @Operation(
            description = "Update product",
            summary = "Responsible for update an product",
            responses = {
                    @ApiResponse(description = "Product updated", responseCode = "201"),
                    @ApiResponse(
                            description = "When the request is sent without the body or with some attribute missing",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"error\": \"The request body has some errors or doesn't exist\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "When the attribute has an invalid format",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"errors\": \"code: 008 message: error message\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<ProductResponseDTO> update(UUID productId, ProductRequestDTO productRequestDTO);

    @Operation(
            description = "Find product by ID",
            summary = "Responsible for search an product by ID",
            responses = {
                    @ApiResponse(description = "Product found", responseCode = "200"),
                    @ApiResponse(
                            description = "When the attribute has an invalid format",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"errors\": \"code: 008 message: error message\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<ProductResponseDTO> findById(UUID productId);

    @Operation(
            description = "Find all products",
            summary = "Responsible for search all paginated products, "
                    + "requires page information and result quantities per page",
            responses = {
                    @ApiResponse(description = "Product found", responseCode = "200"),
                    @ApiResponse(
                            description = "When the attribute has an invalid format",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"errors\": \"code: 008 message: error message\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Page<ProductResponseDTO>> findAll(int page, int size);

    @Operation(
            description = "Remove an product",
            summary = "Responsible for delete an product, "
                    + "requires the product id",
            responses = {
                    @ApiResponse(description = "Product deleted", responseCode = "200"),
                    @ApiResponse(
                            description = "When the attribute has an invalid format",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"errors\": \"code: 008 message: error message\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Void> delete(UUID productId);
}
