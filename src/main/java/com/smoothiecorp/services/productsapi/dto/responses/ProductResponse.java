package com.smoothiecorp.services.productsapi.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 3927711412727106270L;

    private String name;

    private String description;

    private String sku;

    private String code;

    private BigDecimal price;

    private Integer quantity;
}