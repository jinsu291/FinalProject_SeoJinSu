package com.example.finalproject.finalproject.app.cash.entity;

import com.example.finalproject.finalproject.app.base.entity.BaseEntity;
import com.example.finalproject.finalproject.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class CashLog extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member member;
    private String relTypeCode;
    private Long relId;
    private long price; // 변동
    private String eventType;

    public CashLog(long id) {
        super(id);
    }
}
