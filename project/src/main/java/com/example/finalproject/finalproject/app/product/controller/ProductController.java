package com.example.finalproject.finalproject.app.product.controller;

import com.example.finalproject.finalproject.app.base.exception.ActorCanNotModifyException;
import com.example.finalproject.finalproject.app.base.exception.ActorCanNotSeeException;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.product.entity.Product;
import com.example.finalproject.finalproject.app.product.form.ProductForm;
import com.example.finalproject.finalproject.app.product.service.ProductService;
import com.example.finalproject.finalproject.app.security.dto.MemberContext;
import com.example.finalproject.finalproject.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String showCreate() {
        return "product/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@AuthenticationPrincipal MemberContext memberContext, @Valid ProductForm productForm) {
        Member author = memberContext.getMember();
        Product product = productService.create(author, productForm.getSubject(), productForm.getPrice());
        return "redirect:/product/" + product.getId() + "?msg=" + Ut.url.encode("%d번 상품이 생성되었습니다.".formatted(product.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Product product = productService.findForPrintById(id).get();

        Member actor = memberContext.getMember();

        if (productService.actorCanModify(actor, product) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("product", product);

        return "product/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@AuthenticationPrincipal MemberContext memberContext, @Valid ProductForm productForm, @PathVariable long id) {
        Product product = productService.findById(id).get();
        Member actor = memberContext.getMember();

        if (productService.actorCanModify(actor, product) == false) {
            throw new ActorCanNotModifyException();
        }

        productService.modify(product, productForm.getSubject(), productForm.getPrice());
        return "redirect:/product/" + product.getId() + "?msg=" + Ut.url.encode("%d번 상품이 생성되었습니다.".formatted(product.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id, Model model) {
        Product product = productService.findForPrintById(id).get();

        Member actor = memberContext.getMember();

        if (productService.actorCanModify(actor, product) == false) {
            throw new ActorCanNotSeeException();
        }

        model.addAttribute("product", product);

        return "product/detail";
    }
}
