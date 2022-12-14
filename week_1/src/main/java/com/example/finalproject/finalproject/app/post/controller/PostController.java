package com.example.finalproject.finalproject.app.post.controller;

import com.example.finalproject.finalproject.app.base.exception.ActorCanNotModifyException;
import com.example.finalproject.finalproject.app.base.exception.ActorCanNotRemoveException;
import com.example.finalproject.finalproject.app.base.rq.Rq;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.post.entity.Post;
import com.example.finalproject.finalproject.app.post.form.PostForm;
import com.example.finalproject.finalproject.app.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String getWrite() {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid PostForm postForm) {
        Member author = rq.getMember();
        Post post = postService.write(author, postForm.getSubject(), postForm.getContent(), postForm.getContentHtml());
        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글이 생성되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, Model model) {
        Post post = postService.findForPrintById(id).get();

        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("post", post);

        return "post/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@Valid PostForm postForm, @PathVariable long id) {
        Post post = postService.findById(id).get();
        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        postService.modify(post, postForm.getSubject(), postForm.getContent(), postForm.getContentHtml());
        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글이 수정되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String getPostDetail(@PathVariable Long id, Model model) {
        Post post = postService.findForPrintById(id).get();

        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("post", post);
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/remove")
    public String remove(@PathVariable long id) {
        Post post = postService.findById(id).get();
        Member actor = rq.getMember();

        if (postService.actorCanRemove(actor, post) == false) {
            throw new ActorCanNotRemoveException();
        }

        postService.remove(post);

        return Rq.redirectWithMsg("/post/list", "%d번 글이 삭제되었습니다.".formatted(post.getId()));
    }
}