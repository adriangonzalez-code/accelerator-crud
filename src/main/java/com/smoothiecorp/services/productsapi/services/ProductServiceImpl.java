package com.smoothiecorp.services.productsapi.services;

import com.smoothiecorp.services.productsapi.aspects.LogExecution;
import com.smoothiecorp.services.productsapi.dto.requests.CreateProductRequest;
import com.smoothiecorp.services.productsapi.dto.requests.UpdateProductRequest;
import com.smoothiecorp.services.productsapi.dto.responses.ProductResponse;
import com.smoothiecorp.services.productsapi.mappers.ProductRequestToProductMapper;
import com.smoothiecorp.services.productsapi.mappers.ProductToProductResponseMapper;
import com.smoothiecorp.services.productsapi.models.Product;
import com.smoothiecorp.services.productsapi.repositories.IProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository repository;

    @Autowired
    private ProductToProductResponseMapper responseMapper;

    @Autowired
    private ProductRequestToProductMapper requestMapper;

    @Override
    @Transactional(readOnly = true)
    @LogExecution
    public ProductResponse getProductByCode(String code) {
        return this.repository.findByCode(code)
                .map(p -> this.responseMapper.productToProductResponse(p))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    @LogExecution
    public List<ProductResponse> getAllProducts() {
        return this.repository.findAll().stream()
                .map(p -> this.responseMapper.productToProductResponse(p))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @LogExecution
    public ProductResponse createProduct(CreateProductRequest request) {
        ProductResponse response;
        Product product = this.requestMapper.productRequestToProduct(request);
        product = this.repository.save(product);
        response = this.responseMapper.productToProductResponse(product);

        return response;
    }

    @Override
    @Transactional
    @LogExecution
    public ProductResponse updateProduct(String code, UpdateProductRequest request) {
        AtomicReference<ProductResponse> response = new AtomicReference<>();
        this.repository.findByCode(code)
                .ifPresentOrElse(p -> {
                    this.requestMapper.productRequestToProduct(request, p);
                    p = this.repository.save(p);
                    response.set(this.responseMapper.productToProductResponse(p));
                }, () -> response.set(null));

        return response.get();
    }

    @Override
    @Transactional
    @LogExecution
    public boolean deleteProductByCode(String code) {
        AtomicBoolean result = new AtomicBoolean();

        this.repository.findByCode(code)
                .ifPresentOrElse(p -> {
                            this.repository.delete(p);
                            result.set(true);
                        },
                        () -> result.set(false));

        return result.get();
    }
}