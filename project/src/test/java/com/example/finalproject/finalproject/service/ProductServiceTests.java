package com.example.finalproject.finalproject.service;

import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.repository.MemberRepository;
import com.example.finalproject.finalproject.product.entity.Product;
import com.example.finalproject.finalproject.product.service.ProductService;
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
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("상품 등록")
    void t1() {
        Member author = memberRepository.findByUsername("user1").get();

        Product product3 = productService.create(author, "상품명3", 50_000);
        Product product4 = productService.create(author, "상품명4", 60_000);

        assertThat(product3).isNotNull();
        assertThat(product4).isNotNull();
    }
}
