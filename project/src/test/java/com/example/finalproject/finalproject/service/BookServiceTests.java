package com.example.finalproject.finalproject.service;

import com.example.finalproject.finalproject.app.book.entity.Book;
import com.example.finalproject.finalproject.app.book.service.BookService;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.repository.MemberRepository;
import com.example.finalproject.finalproject.app.product.entity.Product;
import com.example.finalproject.finalproject.app.product.service.ProductService;
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
public class BookServiceTests {
    @Autowired
    private BookService bookService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("상품 등록")
    void t1() {
        Member author = memberRepository.findByUsername("user1").get();

        Book book = bookService.create(author, "제목", 3000);

        assertThat(book).isNotNull();
        assertThat(book.getSubject()).isEqualTo("제목");
        assertThat(book.getPrice()).isEqualTo(3000);
    }
}
