package com.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductDto {

    @NotEmpty(message = "Product name is mandatory")
    private String name;
    private String description;
    @NotNull(message = "Product price is mandatory")
    private BigDecimal price;
}
