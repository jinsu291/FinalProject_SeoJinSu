package com.example.finalproject.finalproject.app.base.initData;

import com.example.finalproject.finalproject.app.cart.entity.CartItem;
import com.example.finalproject.finalproject.app.cart.service.CartService;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.service.MemberService;
import com.example.finalproject.finalproject.app.post.service.PostService;
import com.example.finalproject.finalproject.product.entity.Product;
import com.example.finalproject.finalproject.product.service.ProductService;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService, ProductService productService, CartService cartService) {
        Member member1 = memberService.join("user1", "1234", "user1@test.com", "");
        Member member2 = memberService.join("user2", "1234", "user2@test.com", "user2");

        Product product1 = productService.create(member1, "상품명1 상품명1 상품명1 상품명1 상품명1 상품명1", 30_000);
        Product product2 = productService.create(member2, "상품명2", 40_000);
        Product product3 = productService.create(member1, "상품명3", 50_000);
        Product product4 = productService.create(member2, "상품명4", 60_000);

        CartItem cartItem1 = cartService.addItem(member1, product1);
        CartItem cartItem2 = cartService.addItem(member1, product2);
        CartItem cartItem3 = cartService.addItem(member2, product3);
        CartItem cartItem4 = cartService.addItem(member2, product4);
    }
}
