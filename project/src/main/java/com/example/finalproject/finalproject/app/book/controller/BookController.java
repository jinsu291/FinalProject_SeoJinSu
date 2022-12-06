package com.example.finalproject.finalproject.app.book.controller;

import com.example.finalproject.finalproject.app.base.exception.ActorCanNotModifyException;
import com.example.finalproject.finalproject.app.base.exception.ActorCanNotSeeException;
import com.example.finalproject.finalproject.app.book.entity.Book;
import com.example.finalproject.finalproject.app.book.form.BookForm;
import com.example.finalproject.finalproject.app.book.service.BookService;
import com.example.finalproject.finalproject.app.member.entity.Member;
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
@RequestMapping("/book")
@Slf4j
public class BookController {
    private final BookService bookService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String showCreate() {
        return "book/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@AuthenticationPrincipal MemberContext memberContext, @Valid BookForm bookForm) {
        Member author = memberContext.getMember();
        Book book = bookService.create(author, bookForm.getSubject(), bookForm.getPrice());
        return "redirect:/book/" + book.getId() + "?msg=" + Ut.url.encode("%d번 도서가 생성되었습니다.".formatted(book.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Book book = bookService.findForPrintById(id).get();

        Member actor = memberContext.getMember();

        if (bookService.actorCanModify(actor, book) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("book", book);

        return "book/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@AuthenticationPrincipal MemberContext memberContext, @Valid BookForm bookForm, @PathVariable long id) {
        Book book = bookService.findById(id).get();
        Member actor = memberContext.getMember();

        if (bookService.actorCanModify(actor, book) == false) {
            throw new ActorCanNotModifyException();
        }

        bookService.modify(book, bookForm.getSubject(), bookForm.getPrice());
        return "redirect:/book/" + book.getId() + "?msg=" + Ut.url.encode("%d번 도서가 생성되었습니다.".formatted(book.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String showList(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member actor = memberContext.getMember();

        List<Book> books = bookService.findAllByAuthorId(actor.getId());

        model.addAttribute("books", books);

        return "book/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id, Model model) {
        Book book = bookService.findForPrintById(id).get();

        Member actor = memberContext.getMember();

        if (bookService.actorCanModify(actor, book) == false) {
            throw new ActorCanNotSeeException();
        }

        model.addAttribute("book", book);

        return "book/detail";
    }
}
