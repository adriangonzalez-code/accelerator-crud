package com.smoothiecorp.services.productsapi.repositories;

import com.smoothiecorp.services.productsapi.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);
}