package com.smoothiecorp.services.productsapi.mappers;

import com.smoothiecorp.services.productsapi.dto.responses.ProductResponse;
import com.smoothiecorp.services.productsapi.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductToProductResponseMapper {

    ProductResponse productToProductResponse(Product product);
}