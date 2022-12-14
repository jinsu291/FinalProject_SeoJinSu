package com.example.finalproject.finalproject.app.member.entity;

import com.example.finalproject.finalproject.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String nickname;
    private boolean emailVerified;
    private long restCash;

    public String getName() {
//        if (nickname != null) {
//            return nickname;
//        }

        return username;
    }

    public Member(long id) {
        super(id);
    }
}