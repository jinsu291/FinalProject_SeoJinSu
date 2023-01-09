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

import java.util.List;

public interface InitDataBefore {
    default void before(
            MemberRepository memberRepository,
            MemberService memberService,
            PostService postService,
            ProductService productService,
            CartService cartService,
            OrderService orderService) {

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

//        MyBook myBook1 = myBookService.create(member1, "책 1", 30_000);
//        MyBook myBook2 = myBookService.create(member1, "책 2", 40_000);
//        MyBook myBook3 = myBookService.create(member2, "책 3", 50_000);
//        MyBook myBook4 = myBookService.create(member2, "책 4", 60_000);
//        MyBook myBook5 = myBookService.create(member1, "책 5", 70_000);
//        MyBook myBook6 = myBookService.create(member1, "책 6", 80_000);
//        MyBook myBook7 = myBookService.create(member2, "책 7", 90_000);
//        MyBook myBook8 = myBookService.create(member2, "책 8", 100_000);

        Product product1 = productService.create(member1, "상품명1", 30_000, "자바", "IT");

        memberService.addCash(member1, 10_000, "충전__무통장입금");
        memberService.addCash(member1, 20_000, "충전__무통장입금");
        memberService.addCash(member1, -5_000, "출금__일반");
        memberService.addCash(member1, 1_000_000, "충전__무통장입금");
        memberService.addCash(member2, 2_000_000, "충전__무통장입금");

//        // 1번 주문 : 결제완료
//        Order order1 = helper.order(member1, Arrays.asList(
//                        product1,
//                        product2
//                )
//        );
//
//        int order1PayPrice = order1.calculatePayPrice();
//        orderService.payByRestCashOnly(order1);
//
//        // 2번 주문 : 결제 후 환불
//        Order order2 = helper.order(member2, Arrays.asList(
//                        product3,
//                        product4
//                )
//        );
//
//        orderService.payByRestCashOnly(order2);
//
//        orderService.refund(order2);
//
//        // 3번 주문 : 결제 전
//        Order order3 = helper.order(member2, Arrays.asList(
//                        product1,
//                        product2
//                )
//        );
//
//        cartService.addItem(member1, product1);
//        cartService.addItem(member1, product2);
//        cartService.addItem(member1, product3);
//        cartService.addItem(member2, product4);
    }
}
