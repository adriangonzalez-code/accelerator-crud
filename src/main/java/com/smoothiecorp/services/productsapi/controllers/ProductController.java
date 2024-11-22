package com.smoothiecorp.services.productsapi.controllers;

import com.smoothiecorp.services.productsapi.dto.requests.CreateProductRequest;
import com.smoothiecorp.services.productsapi.dto.requests.UpdateProductRequest;
import com.smoothiecorp.services.productsapi.dto.responses.ProductResponse;
import com.smoothiecorp.services.productsapi.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1*/products")
public class ProductController {

    @Autowired
    private IProductService service;

    @GetMapping("/{code}")
    public ResponseEntity<?> getProductByCode(@PathVariable String code) {
        ProductResponse product = this.service.getProductByCode(code);

        if (product == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");

        return ResponseEntity.ok().body(product);
    }

    @GetMapping
    public ResponseEntity<?> getProducts() {
        List<ProductResponse> products = this.service.getAllProducts();
        return ResponseEntity.ok().body(products);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest request) {
        ProductResponse product = this.service.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateProduct(@PathVariable String code, @RequestBody UpdateProductRequest request) {
        ProductResponse product = this.service.updateProduct(code, request);

        if (product == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");

        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteProductByCode(@PathVariable String code) {
        boolean deleted = this.service.deleteProductByCode(code);

        if (deleted) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");
    }
}