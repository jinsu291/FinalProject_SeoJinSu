package com.example.finalproject.finalproject.app.home.controller;

import com.example.finalproject.finalproject.app.base.rq.Rq;
import com.example.finalproject.finalproject.app.post.entity.Post;
import com.example.finalproject.finalproject.app.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/")
    public String showMain(Model model) {
        if ( rq.isLogined() ) {
            List<Post> posts = postService.findAllForPrintByAuthorIdOrderByIdDesc(rq.getId());
            model.addAttribute("posts", posts);
        }

        return "home/main";
    }
}