package com.example.finalproject.finalproject.app.ProductKeyword.repository;


import com.example.finalproject.finalproject.app.ProductKeyword.entity.ProductKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductKeywordRepository extends JpaRepository<ProductKeyword, Long> {
    Optional<ProductKeyword> findByContent(String keywordContent);
}
