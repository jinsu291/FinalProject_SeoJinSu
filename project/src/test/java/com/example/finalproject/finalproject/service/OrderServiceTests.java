package com.example.finalproject.finalproject.service;


import com.example.finalproject.finalproject.app.cart.service.CartService;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.service.MemberService;
import com.example.finalproject.finalproject.app.order.entity.Order;
import com.example.finalproject.finalproject.app.order.service.OrderService;
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
public class OrderServiceTests {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("주문")
    void t1() {
        // 상품 3, 상품 4 불러오기
        Product product3 = productService.findById(3).get();
        Product product4 = productService.findById(4).get();

        // 2번 회원 불러오기
        Member member2 = memberService.findById(2).get();

        // 2번 회원의 장바구니에 상품 2개 추가
        cartService.addItem(member2, product3);
        cartService.addItem(member2, product4);

        // 2번회원의 장바구니에 있는 상품으로 주문 생성
        Order order = orderService.createFromCart(member2);

        assertThat(order).isNotNull();
    }
}
