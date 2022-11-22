package com.example.finalproject.finalproject.app.post.controller;

import com.example.finalproject.finalproject.app.post.dto.PostWriteReq;
import com.example.finalproject.finalproject.app.post.entity.Post;
import com.example.finalproject.finalproject.app.post.service.PostService;
import com.example.finalproject.finalproject.app.security.dto.MemberContext;
import com.example.finalproject.finalproject.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String getWrite() {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String postWrite(@AuthenticationPrincipal MemberContext memberContext, @Valid PostWriteReq postWriteReq) {
        Post post = postService.write(memberContext.getId(),postWriteReq.getSubject(),postWriteReq.getContent()) ;
        String msg = "%d번 게시물이 작성되었습니다.".formatted(post.getId());
        msg = Ut.url.encode(msg);
        return "redirect:/post/%d?msg=%s".formatted(post.getId(), msg);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String getPostDetail() {
        return "post/write";
    }
}