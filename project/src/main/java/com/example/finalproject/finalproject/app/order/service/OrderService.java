package com.example.finalproject.finalproject.app.order.service;

import com.example.finalproject.finalproject.app.base.dto.RsData;
import com.example.finalproject.finalproject.app.cart.entity.CartItem;
import com.example.finalproject.finalproject.app.cart.service.CartService;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.service.MemberService;
import com.example.finalproject.finalproject.app.myBook.service.MyBookService;
import com.example.finalproject.finalproject.app.order.entity.Order;
import com.example.finalproject.finalproject.app.order.entity.OrderItem;
import com.example.finalproject.finalproject.app.order.repository.OrderItemRepository;
import com.example.finalproject.finalproject.app.order.repository.OrderRepository;
import com.example.finalproject.finalproject.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.finalproject.finalproject.app.AppConfig.cancelAvailableSeconds;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final MemberService memberService;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MyBookService myBookService;

    @Transactional
    public Order createFromCart(Member buyer) {
        // 입력된 회원의 장바구니 아이템들을 전부 가져온다.

        // 만약에 특정 장바구니의 상품옵션이 판매불능이면 삭제
        // 만약에 특정 장바구니의 상품옵션이 판매가능이면 주문품목으로 옮긴 후 삭제

        List<CartItem> cartItems = cartService.getItemsByBuyer(buyer);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.isOrderable()) {
                orderItems.add(new OrderItem(product));
            }

            cartService.removeItem(cartItem);
        }

        return create(buyer, orderItems);
    }

    @Transactional
    public Order create(Member buyer, List<OrderItem> orderItems) {
        Order order = Order
                .builder()
                .buyer(buyer)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        // 주문 품목으로 부터 이름을 만든다.
        order.makeName();

        orderRepository.save(order);

        return order;
    }

    @Transactional
    public void payByRestCashOnly(Order order) {
        Member buyer = order.getBuyer();

        long restCash = buyer.getRestCash();

        int payPrice = order.calculatePayPrice();

        if (payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }

        memberService.addCash(buyer, payPrice * -1, "주문__%d__사용__예치금".formatted(order.getId()));

        order.setPaymentDone();
        orderRepository.save(order);
    }

    @Transactional
    public RsData refund(Order order, Member actor) {
        RsData actorCanRefundRsData = actorCanRefund(actor, order);

        if (actorCanRefundRsData.isFail()) {
            return actorCanRefundRsData;
        }

        order.setCancelDone();

        int payPrice = order.getPayPrice();
        memberService.addCash(order.getBuyer(), payPrice, "주문__%d__환불__예치금".formatted(order.getId()));

        order.setRefundDone();
        orderRepository.save(order);

        myBookService.remove(order);

        return RsData.of("S-1", "환불되었습니다.");
    }

    @Transactional
    public RsData refund(Long orderId, Member actor) {
        Order order = findById(orderId).orElse(null);

        if (order == null) {
            return RsData.of("F-2", "결제 상품을 찾을 수 없습니다.");
        }
        return refund(order, actor);
    }

    public RsData actorCanRefund(Member actor, Order order) {

        if (order.isCanceled()) {
            return RsData.of("F-1", "이미 취소되었습니다.");
        }

        if (order.isRefunded()) {
            return RsData.of("F-4", "이미 환불되었습니다.");
        }

        if (order.isPaid() == false) {
            return RsData.of("F-5", "결제가 되어야 환불이 가능합니다.");
        }

        if (actor.getId().equals(order.getBuyer().getId()) == false) {
            return RsData.of("F-2", "권한이 없습니다.");
        }

        long between = ChronoUnit.SECONDS.between(order.getPayDate(), LocalDateTime.now());

        if (between > cancelAvailableSeconds) {
            return RsData.of("F-3", "결제 된지 %d분이 지났으므로, 환불 할 수 없습니다.".formatted(between / 60));
        }

        return RsData.of("S-1", "환불할 수 있습니다.");
    }

    public Optional<Order> findForPrintById(long id) {
        return findById(id);
    }

    private Optional<Order> findById(long id) {
        return orderRepository.findById(id);
    }

    public boolean actorCanSee(Member actor, Order order) {
        return actor.getId().equals(order.getBuyer().getId());
    }

    @Transactional
    public void payByTossPayments(Order order, long useRestCash) {
        Member buyer = order.getBuyer();
        int payPrice = order.calculatePayPrice();

        long pgPayPrice = payPrice - useRestCash;
        memberService.addCash(buyer, pgPayPrice, "주문__%d__충전__토스페이먼츠".formatted(order.getId()));
        memberService.addCash(buyer, pgPayPrice * -1, "주문__%d__사용__토스페이먼츠".formatted(order.getId()));

        if ( useRestCash > 0 ) {
            memberService.addCash(buyer, useRestCash * -1, "주문__%d__사용__예치금".formatted(order.getId()));
        }

        order.setPaymentDone();
        orderRepository.save(order);
    }

    public boolean actorCanPayment(Member actor, Order order) {
        return actorCanSee(actor, order);
    }

    public List<OrderItem> findAllByPayDateBetweenOrderByIdAsc(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderItemRepository.findAllByPayDateBetween(fromDate, toDate);
    }

    public List<Order> findAllByBuyerId(long buyerId) {
        return orderRepository.findAllByBuyerIdOrderByIdDesc(buyerId);
    }

    @Transactional
    public RsData cancel(Order order, Member actor) {
        RsData actorCanCancelRsData = actorCanCancel(actor, order);
        if (actorCanCancelRsData.isFail()) {
            return actorCanCancelRsData;
        }
        order.setCanceled(true);

        return RsData.of("S-1", "취소되었습니다.");
    }

    @Transactional
    public RsData cancel(Long orderId, Member actor) {
        Order order = findById(orderId).get();
        return cancel(order, actor);
    }

    public RsData actorCanCancel(Member actor, Order order) {
        if (order.isPaid()) {
            return RsData.of("F-3", "이미 결제처리 되었습니다.");
        }

        if (order.isCanceled()) {
            return RsData.of("F-1", "이미 취소되었습니다.");
        }

        if (actor.getId().equals(order.getBuyer().getId()) == false) {
            return RsData.of("F-2", "권한이 없습니다.");
        }

        return RsData.of("S-1", "취소할 수 있습니다.");
    }

}
