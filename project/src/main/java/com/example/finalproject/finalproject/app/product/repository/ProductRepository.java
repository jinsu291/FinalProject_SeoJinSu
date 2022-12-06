package com.example.finalproject.finalproject.app.product.repository;

import com.example.finalproject.finalproject.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
