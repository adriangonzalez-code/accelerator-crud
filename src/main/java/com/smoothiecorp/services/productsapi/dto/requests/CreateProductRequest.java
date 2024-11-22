package com.smoothiecorp.services.productsapi.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateProductRequest {

    private String name;
    private String description;
    private String code;
    private BigDecimal price;
    private Integer quantity;
}