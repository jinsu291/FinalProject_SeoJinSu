package com.example.finalproject.finalproject.app.book.controller;

import com.example.finalproject.finalproject.app.base.exception.ActorCanNotModifyException;
import com.example.finalproject.finalproject.app.base.exception.ActorCanNotRemoveException;
import com.example.finalproject.finalproject.app.base.rq.Rq;
import com.example.finalproject.finalproject.app.book.entity.Book;
import com.example.finalproject.finalproject.app.book.form.BookForm;
import com.example.finalproject.finalproject.app.book.service.BookService;
import com.example.finalproject.finalproject.app.member.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String showCreate() {
        return "book/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid BookForm bookForm) {
        Member author = rq.getMember();
        Book book = bookService.create(author, bookForm.getSubject(), bookForm.getPrice());
        return Rq.redirectWithMsg("/book/" + book.getId(), "%d번 글이 생성되었습니다.".formatted(book.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, Model model) {
        Book book = bookService.findForPrintById(id).get();

        Member actor = rq.getMember();

        if (bookService.actorCanModify(actor, book) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("book", book);

        return "book/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@Valid BookForm bookForm, @PathVariable long id) {
        Book book = bookService.findById(id).get();
        Member actor = rq.getMember();

        if (bookService.actorCanModify(actor, book) == false) {
            throw new ActorCanNotModifyException();
        }

        bookService.modify(book, bookForm.getSubject(), bookForm.getPrice());
        return Rq.redirectWithMsg("/book/" + book.getId(), "%d번 글이 수정되었습니다.".formatted(book.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Book book = bookService.findForPrintById(id).get();

        Member actor = rq.getMember();

        if (bookService.actorCanModify(actor, book) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("book", book);

        return "book/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model) {
        List<Book> books = bookService.findAllForPrintByAuthorIdOrderByIdDesc(rq.getId());

        model.addAttribute("books", books);

        return "book/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/remove")
    public String remove(@PathVariable long id) {
        Book book = bookService.findById(id).get();
        Member actor = rq.getMember();

        if (bookService.actorCanRemove(actor, book) == false) {
            throw new ActorCanNotRemoveException();
        }

        bookService.remove(book);

        return Rq.redirectWithMsg("/book/list", "%d번 글이 삭제되었습니다.".formatted(book.getId()));
    }
}
