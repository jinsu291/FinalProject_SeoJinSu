package com.example.finalproject.finalproject.app.myBook.repository;

import com.example.finalproject.finalproject.app.myBook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    void deleteByProductIdAndOwnerId(Long id, Long id1);
}
