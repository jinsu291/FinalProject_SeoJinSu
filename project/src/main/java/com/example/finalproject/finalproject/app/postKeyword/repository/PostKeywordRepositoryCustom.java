package com.example.finalproject.finalproject.app.postKeyword.repository;

import com.example.finalproject.finalproject.app.postKeyword.entity.PostKeyword;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeyword> getQslAllByAuthorId(Long authorId);
}
