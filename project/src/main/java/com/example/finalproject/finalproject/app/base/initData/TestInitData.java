package com.example.finalproject.finalproject.app.base.initData;

import com.example.finalproject.finalproject.app.myBook.service.MyBookService;
import com.example.finalproject.finalproject.app.cart.service.CartService;
import com.example.finalproject.finalproject.app.member.service.MemberService;
import com.example.finalproject.finalproject.app.order.service.OrderService;
import com.example.finalproject.finalproject.app.post.service.PostService;
import com.example.finalproject.finalproject.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestInitData implements InitDataBefore {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            PostService postService,
            MyBookService myBookService,
            ProductService productService,
            CartService cartService,
            OrderService orderService
            ) {
        return args -> {
            before(memberService, postService, myBookService, productService,  cartService, orderService);
        };
    }
}
