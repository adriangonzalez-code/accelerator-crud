package com.smoothiecorp.services.productsapi.dto.requests;

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
public class UpdateProductRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4162074476575267427L;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
}