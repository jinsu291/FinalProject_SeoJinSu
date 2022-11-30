package com.example.finalproject.finalproject.product.service;

import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.product.entity.Product;
import com.example.finalproject.finalproject.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Product create(Member author, String subject, int price) {
        Product product = Product.builder()
                .author(author)
                .subject(subject)
                .price(price)
                .build();

        productRepository.save(product);

        return product;
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void modify(Product product, String subject, int price) {
        product.setSubject(subject);
        product.setPrice(price);
    }
}
