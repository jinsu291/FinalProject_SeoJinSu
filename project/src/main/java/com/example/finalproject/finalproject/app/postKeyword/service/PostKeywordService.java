package com.example.finalproject.finalproject.app.postKeyword.service;

import com.example.finalproject.finalproject.app.postKeyword.entity.PostKeyword;
import com.example.finalproject.finalproject.app.postKeyword.repository.PostKeywordRepository;

import java.util.Optional;

public class PostKeywordService {
    private PostKeywordRepository postKeywordRepository;

    public PostKeyword save(String content) {
        Optional<PostKeyword> optKeyword = postKeywordRepository.findByContent(content);

        if (optKeyword.isPresent()) {
            return optKeyword.get();
        }

        PostKeyword postKeyword = PostKeyword
                .builder()
                .content(content)
                .build();

        postKeywordRepository.save(postKeyword);

        return postKeyword;
    }
}
