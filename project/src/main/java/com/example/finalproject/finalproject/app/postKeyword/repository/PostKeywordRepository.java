package com.example.finalproject.finalproject.app.postKeyword.repository;

import com.example.finalproject.finalproject.app.postKeyword.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {
    Optional<PostKeyword> findByContent(String keywordContent);
}
