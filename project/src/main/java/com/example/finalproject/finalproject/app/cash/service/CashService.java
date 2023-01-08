package com.example.finalproject.finalproject.app.cash.service;

import com.example.finalproject.finalproject.app.cash.entity.CashLog;
import com.example.finalproject.finalproject.app.cash.repository.CashLogRepository;
import com.example.finalproject.finalproject.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashService {
    private final CashLogRepository cashLogRepository;

    public CashLog addCash(Member member, long price, String eventType) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);
        return cashLog;
    }
}
