package com.example.finalproject.finalproject.service;

import com.example.finalproject.finalproject.app.myBook.entity.MyBook;
import com.example.finalproject.finalproject.app.myBook.service.MyBookService;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MyMyBookServiceTests {
    @Autowired
    private MyBookService myBookService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("상품 등록")
    void t1() {
        Member author = memberRepository.findByUsername("user1").get();

        MyBook myBook = myBookService.create(author, "제목", 3000);

        assertThat(myBook).isNotNull();
        assertThat(myBook.getSubject()).isEqualTo("제목");
        assertThat(myBook.getPrice()).isEqualTo(3000);
    }
}
