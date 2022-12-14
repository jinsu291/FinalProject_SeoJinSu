package com.example.finalproject.finalproject.app.book.service;

import com.example.finalproject.finalproject.app.book.entity.Book;
import com.example.finalproject.finalproject.app.book.repository.BookRepository;
import com.example.finalproject.finalproject.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public Book create(Member author, String subject, int price) {
        Book book = Book.builder()
                .author(author)
                .subject(subject)
                .price(price)
                .build();

        bookRepository.save(book);

        return book;
    }

    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public void modify(Book book, String subject, int price) {
        book.setSubject(subject);
        book.setPrice(price);
    }

    public boolean actorCanModify(Member author, Book book) {
        return author.getId().equals(book.getAuthor().getId());
    }

    public boolean actorCanDelete(Member author, Book book) {
        return actorCanModify(author, book);
    }

    public Optional<Book> findForPrintById(long id) {
        Optional<Book> opBook = findById(id);

        if (opBook.isEmpty()) return opBook;

        return opBook;
    }

    public List<Book> findAllByAuthorId(Long id) {
        return bookRepository.findAllByAuthorId(id);
    }

    public boolean actorCanRemove(Member author, Book book) {
        return actorCanModify(author, book);
    }

    public void remove(Book book) {
        bookRepository.delete(book);
    }

    public List<Book> findAllForPrintByAuthorIdOrderByIdDesc(long authorId) {
        List<Book> books = bookRepository.findAllByAuthorIdOrderByIdDesc(authorId);

        return books;
    }
}
