package com.example.finalproject.finalproject.app.postTag.service;

import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.post.entity.Post;
import com.example.finalproject.finalproject.app.postKeyword.service.PostKeywordService;
import com.example.finalproject.finalproject.app.postTag.entity.PostTag;
import com.example.finalproject.finalproject.app.postTag.repository.PostTagRepository;
import com.example.finalproject.finalproject.app.postKeyword.entity.PostKeyword;
import com.example.finalproject.finalproject.app.productTag.entity.ProductTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostTagService {
    private PostTagRepository postTagRepository;
    private PostKeywordService postKeywordService;

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

    public List<PostTag> getPostTagsByPostIdIn(long[] ids) {
        return postTagRepository.findAllByPostIdIn(ids);
    }

    public List<PostTag> getPostTags(Member member, String postKeywordContent) {
        return postTagRepository.findAllByMemberIdAndPostKeyword_contentOrderByPost_idDesc(member.getId(), postKeywordContent);
    }

    public List<PostTag> getPostTags(long authorId, long postKeywordId) {
        return postTagRepository.findAllByMemberIdAndPostKeywordIdOrderByPost_idDesc(authorId, postKeywordId);
    }

    public List<ProductTag> getPostTags(String productTagContent) {
        return postTagRepository.findAllByPostKeyword_contentOrderByPost_idDesc(productTagContent);
    }
}
