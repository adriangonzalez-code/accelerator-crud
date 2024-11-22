package com.smoothiecorp.services.productsapi.mappers;

import com.smoothiecorp.services.productsapi.dto.requests.CreateProductRequest;
import com.smoothiecorp.services.productsapi.dto.requests.UpdateProductRequest;
import com.smoothiecorp.services.productsapi.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductRequestToProductMapper {

    Product productRequestToProduct(CreateProductRequest request);

    @Mappings({
            @Mapping(source = "request.name", target = "product.name"),
            @Mapping(source = "request.description", target = "product.description"),
            @Mapping(source = "request.price", target = "product.price"),
            @Mapping(source = "request.quantity", target = "product.quantity")
    })
    void productRequestToProduct(UpdateProductRequest request, @MappingTarget Product product);
}