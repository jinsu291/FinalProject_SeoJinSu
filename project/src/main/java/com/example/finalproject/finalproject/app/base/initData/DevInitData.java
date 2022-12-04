package com.example.finalproject.finalproject.app.base.initData;

import com.example.finalproject.finalproject.app.member.service.MemberService;
import com.example.finalproject.finalproject.app.post.service.PostService;
import com.example.finalproject.finalproject.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevInitData implements InitDataBefore {
    @Bean
    CommandLineRunner initData(MemberService memberService, PostService postService, ProductService productService) {
        return args -> {
            before(memberService, postService, productService);
        };
    }
}