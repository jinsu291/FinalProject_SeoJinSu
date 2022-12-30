package com.example.finalproject.finalproject.app.postTag.repository;

import com.example.finalproject.finalproject.app.postTag.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findAllByPostId(Long postId);

    Optional<PostTag> findByPostIdAndPostKeywordId(Long postId, Long keywordId);

    List<PostTag> findAllByPostIdIn(long[] ids);

    List<PostTag> findAllByMemberIdAndPostKeyword_contentOrderByPost_idDesc(Long id, String postKeywordContent);
}
