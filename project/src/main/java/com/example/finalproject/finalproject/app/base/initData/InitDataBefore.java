package com.example.finalproject.finalproject.app.base.initData;

import com.example.finalproject.finalproject.app.book.entity.Book;
import com.example.finalproject.finalproject.app.book.service.BookService;
import com.example.finalproject.finalproject.app.cart.service.CartService;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.service.MemberService;
import com.example.finalproject.finalproject.app.order.entity.Order;
import com.example.finalproject.finalproject.app.order.service.OrderService;
import com.example.finalproject.finalproject.app.post.service.PostService;
import com.example.finalproject.finalproject.app.product.entity.Product;
import com.example.finalproject.finalproject.app.product.service.ProductService;

import java.util.Arrays;
import java.util.List;

public interface InitDataBefore {
    default void before(
            MemberService memberService,
            PostService postService,
            BookService bookService,
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
        Member member2 = memberService.join("user2", "1234", "user2@test.com", "user2");

        Book book1 = bookService.create(member1, "책 1", 30_000);
        Book book2 = bookService.create(member1, "책 2", 40_000);
        Book book3 = bookService.create(member2, "책 3", 50_000);
        Book book4 = bookService.create(member2, "책 4", 60_000);
        Book book5 = bookService.create(member1, "책 5", 70_000);
        Book book6 = bookService.create(member1, "책 6", 80_000);
        Book book7 = bookService.create(member2, "책 7", 90_000);
        Book book8 = bookService.create(member2, "책 8", 100_000);

        Product product1 = productService.create(book1, "책 1", 30_000);
        Product product2 = productService.create(book3, "책 3", 50_000);
        Product product3 = productService.create(book5, "책 5", 70_000);
        Product product4 = productService.create(book7, "책 7", 90_000);

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

        orderService.refund(order2);

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
    }
}
