package com.murilo.market_place.dtos.productcart;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCartDTO implements Serializable {

    @NotNull(message = "The product is required")
    private UUID productId;

    @NotBlank(message = "The product name is required")
    private String productName;

    @NotNull(message = "The product quantity is required")
    @Positive(message = "The product quantity must be positive")
    @Min(value = 1, message = "The product quantity must be at least 1")
    @Max(value = 1000, message = "The product quantity must not exceed 1000")
    private Integer quantity;

    @NotNull(message = "The product price is required")
    @DecimalMin(value = "0.01", message = "The product price must be at least 0.01")
    @DecimalMax(value = "9999.99", message = "The product price must not exceed 9999.99")
    @Digits(integer = 4, fraction = 2, message = "The product price must have up to 10 digits and 2 decimal")
    private BigDecimal price;
}