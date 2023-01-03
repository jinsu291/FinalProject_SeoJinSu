package com.example.finalproject.finalproject.app.product.controller;

import com.example.finalproject.finalproject.app.base.exception.ActorCanNotModifyException;
import com.example.finalproject.finalproject.app.base.rq.Rq;
import com.example.finalproject.finalproject.app.book.entity.Book;
import com.example.finalproject.finalproject.app.book.service.BookService;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.post.entity.Post;
import com.example.finalproject.finalproject.app.product.entity.Product;
import com.example.finalproject.finalproject.app.product.form.ProductForm;
import com.example.finalproject.finalproject.app.product.form.ProductModifyForm;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final BookService bookService;
    private final ProductService productService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @GetMapping("/create")
    public String showCreate(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member actor = memberContext.getMember();

        List<Book> books = bookService.findAllByAuthorId(actor.getId());

        model.addAttribute("books", books);

        return "product/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@AuthenticationPrincipal MemberContext memberContext, @Valid ProductForm productForm) {
        Member author = memberContext.getMember();

        Book book = bookService.findById(productForm.getBookId()).get();

        if (author.getId().equals(book.getAuthor().getId()) == false) {
            return "redirect:/product/create?msg=" + Ut.url.encode("%d번 상품에 대한 권한이 없습니다.".formatted(book.getId()));
        }

        Product product = productService.create(book, productForm.getSubject(), productForm.getPrice());
        return "redirect:/product/" + product.getId() + "?msg=" + Ut.url.encode("%d번 상품이 생성되었습니다.".formatted(product.getId()));
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Product product = productService.findForPrintById(id, rq.getMember()).get();

        model.addAttribute("product", product);

        return "product/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, Model model) {
        Product product = productService.findForPrintById(id, rq.getMember()).get();

        if (productService.actorCanModify(rq.getMember(), product) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("product", product);

        return "product/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@Valid ProductModifyForm productForm, @PathVariable long id) {
        Product product = productService.findById(id).get();
        Member actor = rq.getMember();

        if (productService.actorCanModify(actor, product) == false) {
            throw new ActorCanNotModifyException();
        }

        productService.modify(product, productForm.getSubject(), productForm.getPrice());
        return Rq.redirectWithMsg("/product/" + product.getId(), "%d번 도서 상품이 수정되었습니다.".formatted(product.getId()));
    }
}
