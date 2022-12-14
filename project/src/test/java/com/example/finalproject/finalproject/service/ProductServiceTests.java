package com.example.finalproject.finalproject.service;

import com.example.finalproject.finalproject.app.book.entity.Book;
import com.example.finalproject.finalproject.app.book.service.BookService;
import com.example.finalproject.finalproject.app.product.entity.Product;
import com.example.finalproject.finalproject.app.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ProductServiceTests {
    @Autowired
    private BookService bookService;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 등록")
    void t1() {
        Book book = bookService.findById(1).get();

        Product product = productService.create(book, "그리움", 1_900);

        assertThat(product).isNotNull();
        assertThat(product.getSubject()).isEqualTo("그리움");
        assertThat(product.getPrice()).isEqualTo(1_900);
    }

    @Test
    @DisplayName("상품 수정")
    void t2() {
        Product product1 = productService.findById(1).get();

        productService.modify(product1, "깊은 그리움", 3_200);

        assertThat(product1).isNotNull();
        assertThat(product1.getSubject()).isEqualTo("깊은 그리움");
        assertThat(product1.getPrice()).isEqualTo(3_200);
    }
}

