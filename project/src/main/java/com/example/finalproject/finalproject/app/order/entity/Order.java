package com.example.finalproject.finalproject.app.order.entity;

import com.example.finalproject.finalproject.app.base.entity.BaseEntity;
import com.example.finalproject.finalproject.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Table(name = "product_order")
public class Order extends BaseEntity {
    private LocalDateTime refundDate;
    private LocalDateTime payDate;
    private LocalDateTime cancelDate;

    @ManyToOne(fetch = LAZY)
    private Member buyer;

    private String name;

    private boolean isPaid; // 결제여부
    private boolean isCanceled; // 취소여부
    private boolean isRefunded; // 환불여부

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);

        orderItems.add(orderItem);
    }

    public int calculatePayPrice() {
        int payPrice = 0;

        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getSalePrice();
        }

        return payPrice;
    }

    public void setCancelDone() {
        cancelDate = LocalDateTime.now();

        isCanceled = true;
    }

    public void setPaymentDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setPaymentDone();
        }

        isPaid = true;
    }

    public void setRefundDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setRefundDone();
        }

        isRefunded = true;
    }

    public int getPayPrice() {
        int payPrice = 0;
        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getPayPrice();
        }

        return payPrice;
    }

    public void makeName() {
        String name = orderItems.get(0).getProduct().getSubject();

        if (orderItems.size() > 1) {
            name += " 외 %d권".formatted(orderItems.size() - 1);
        }

        this.name = name;
    }

    public boolean isPayable() {
        if ( isPaid ) return false;
        if ( isCanceled ) return false;

        return true;
    }
}
