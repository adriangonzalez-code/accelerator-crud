package com.smoothiecorp.services.productsapi.services;

import com.smoothiecorp.services.productsapi.dto.requests.CreateProductRequest;
import com.smoothiecorp.services.productsapi.dto.requests.UpdateProductRequest;
import com.smoothiecorp.services.productsapi.dto.responses.ProductResponse;

import java.util.List;

public interface IProductService {

    ProductResponse getProductByCode(String sku);

    List<ProductResponse> getAllProducts();

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse updateProduct(String code, UpdateProductRequest request);

    boolean deleteProductByCode(String code);
}