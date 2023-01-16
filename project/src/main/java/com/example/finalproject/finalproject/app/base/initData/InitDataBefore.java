package com.example.finalproject.finalproject.app.base.initData;

import com.example.finalproject.finalproject.app.member.entity.emum.AuthLevel;
import com.example.finalproject.finalproject.app.member.repository.MemberRepository;
import com.example.finalproject.finalproject.app.cart.service.CartService;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.service.MemberService;
import com.example.finalproject.finalproject.app.order.entity.Order;
import com.example.finalproject.finalproject.app.order.service.OrderService;
import com.example.finalproject.finalproject.app.post.service.PostService;
import com.example.finalproject.finalproject.app.product.entity.Product;
import com.example.finalproject.finalproject.app.product.service.ProductService;
import com.example.finalproject.finalproject.app.withdraw.service.WithdrawService;

import java.util.Arrays;
import java.util.List;

public interface InitDataBefore {
    default void before(
            MemberRepository memberRepository,
            MemberService memberService,
            PostService postService,
            ProductService productService,
            CartService cartService,
            OrderService orderService,
            WithdrawService withdrawService) {

        class Helper {
            public Order order(Member member, List<Product> products) {
                for (int i = 0; i < products.size(); i++) {
                    Product product = products.get(i);

                    cartService.addItem(member, product);
                }

                return orderService.createFromCart(member);
            }
        }

        Helper helper = new Helper();

        Member member1 = memberService.join("user1", "1234", "user1@test.com", "");
        member1.setAuthLevel(AuthLevel.ADMIN);
        memberRepository.save(member1);
        Member member2 = memberService.join("user2", "1234", "user2@test.com", "user2");

        postService.write(
                member1,
                "자바의 정석",
                "# 목차",
                "<h1>연산자 등</h1>",
                "#자바 #정석"
        );

        postService.write(
                member1,
                "모던 딥다이브",
                "# 목차",
                "<h1>연산자 등</h1>",
                "#모던딥다이브 #정석"
        );

        postService.write(member2, "제목 3", "내용 3", "내용 3", "#IT# 프론트엔드 #HTML #CSS");
        postService.write(member2, "제목 4", "내용 4", "내용 4", "#IT #스프링부트 #자바");

        Product product1 = productService.create(member2, "상품명2", 40_000, "스프링부트", "#IT #REACT");
        Product product2 = productService.create(member1, "상품명3", 50_000, "REACT", "#IT #REACT");
        Product product3 = productService.create(member1, "상품명3", 50_000, "REACT", "#IT #REACT");
        Product product4 = productService.create(member2, "상품명4", 60_000, "HTML", "#IT #HTML");


        memberService.addCash(member1, 10_000, "충전__무통장입금");
        memberService.addCash(member1, 20_000, "충전__무통장입금");
        memberService.addCash(member1, -5_000, "출금__일반");
        memberService.addCash(member1, 1_000_000, "충전__무통장입금");
        memberService.addCash(member2, 2_000_000, "충전__무통장입금");

        // 1번 주문 : 결제완료
        Order order1 = helper.order(member1, Arrays.asList(
                        product1,
                        product2
                )
        );

        int order1PayPrice = order1.calculatePayPrice();
        orderService.payByRestCashOnly(order1);

        // 2번 주문 : 결제 후 환불
        Order order2 = helper.order(member2, Arrays.asList(
                        product3,
                        product4
                )
        );

        orderService.payByRestCashOnly(order2);

        // 3번 주문 : 결제 전
        Order order3 = helper.order(member2, Arrays.asList(
                        product1,
                        product2
                )
        );

        cartService.addItem(member1, product1);
        cartService.addItem(member1, product2);
        cartService.addItem(member1, product3);
        cartService.addItem(member2, product4);

        cartService.addItem(member1, product4);

        withdrawService.apply("우리은행", "1002333203948", 50000, member1);
        withdrawService.apply("수협은행", "1002333203947", 40000, member1);
        withdrawService.apply("국민은행", "1002333203946", 30000, member2);
        withdrawService.apply("카카오은행", "1002333203945", 20000, member2);
    }
}
