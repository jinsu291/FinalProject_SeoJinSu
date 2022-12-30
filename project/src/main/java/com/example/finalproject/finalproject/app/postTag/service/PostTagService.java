package com.example.finalproject.finalproject.app.postTag.service;

import com.example.finalproject.finalproject.app.ProductKeyword.service.ProductKeywordService;
import com.example.finalproject.finalproject.app.post.entity.Post;
import com.example.finalproject.finalproject.app.postTag.entity.PostTag;
import com.example.finalproject.finalproject.app.postTag.repository.PostTagRepository;
import com.example.finalproject.finalproject.app.postKeyword.entity.PostKeyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostTagService {
    private PostTagRepository postTagRepository;
    private ProductKeywordService postKeywordService;

    public void applyPostTags(Post post, String postTagContents) {
        List<PostTag> oldPostTags = getPostTags(post);

        List<String> postKeywordContents = Arrays.stream(postTagContents.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        List<PostTag> needToDelete = new ArrayList<>();

        oldPostTags
                .stream()
                .filter(oldPostTag -> !postKeywordContents
                        .stream()
                        .anyMatch(s -> s.equals(oldPostTag.getPostKeyword().getContent())))
                .forEach(oldPostTag -> needToDelete.add(oldPostTag));

        needToDelete.forEach(postTag -> postTagRepository.delete(postTag));

        postKeywordContents.forEach(postKeywordContent -> {
            savePostTag(post, postKeywordContent);
        });
    }

    private PostTag savePostTag(Post post, String postKeywordContent) {
        PostKeyword postKeyword = postKeywordService.save(postKeywordContent);

        Optional<PostTag> opPostTag = postTagRepository.findByPostIdAndPostKeywordId(post.getId(), postKeyword.getId());

        if (opPostTag.isPresent()) {
            return opPostTag.get();
        }

        PostTag postTag = PostTag.builder()
                .post(post)
                .member(post.getAuthor())
                .postKeyword(postKeyword)
                .build();

        postTagRepository.save(postTag);

        return postTag;
    }

    public List<PostTag> getPostTags(Post post) {
        return postTagRepository.findAllByPostId(post.getId());
    }
}
