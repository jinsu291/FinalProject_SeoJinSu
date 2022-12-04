package com.example.finalproject.finalproject.app.base.initData;

import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.service.MemberService;
import com.example.finalproject.finalproject.app.post.service.PostService;
import com.example.finalproject.finalproject.product.service.ProductService;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService, ProductService productService) {
        Member member1 = memberService.join("user1", "1234", "user1@test.com", "");
        Member member2 = memberService.join("user2", "1234", "user2@test.com", "user2");

    }
}
