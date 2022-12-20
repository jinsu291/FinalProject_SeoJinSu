package com.example.finalproject.finalproject.app.book.entity;

import com.example.finalproject.finalproject.app.base.entity.BaseEntity;
import com.example.finalproject.finalproject.app.cart.entity.CartItem;
import com.example.finalproject.finalproject.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import java.util.Map;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
public class Book extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member author;
    private String subject;
    private int price;

    public Book(long id) {
        super(id);
    }

    public int getSalePrice() {
        return getPrice();
    }

    public int getWholesalePrice() {
        return (int) Math.ceil(getPrice() * 0.7);
    }

    public boolean isOrderable() {
        return true;
    }

    public String getJdenticon() {
        return "book__" + getId();
    }

    public CartItem getExtra_actor_cartItem() {
        Map<String, Object> extra = getExtra();

        if (extra.containsKey("actor_cartItem") == false) {
            return null;
        }

        return (CartItem)extra.get("actor_cartItem");
    }

    public boolean getExtra_actor_hasInCart() {
        return getExtra_actor_cartItem() != null;
    }
}