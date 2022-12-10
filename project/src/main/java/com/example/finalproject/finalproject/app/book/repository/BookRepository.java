package com.example.finalproject.finalproject.app.book.repository;

import com.example.finalproject.finalproject.app.book.entity.Book;
import com.example.finalproject.finalproject.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByOrderByIdDesc();
    List<Book> findAllByAuthorId(Long id);

    List<Book> findAllByAuthorIdOrderByIdDesc(long authorId);
}
