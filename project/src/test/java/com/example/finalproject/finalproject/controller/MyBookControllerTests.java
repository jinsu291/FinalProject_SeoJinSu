package com.example.finalproject.finalproject.controller;

import com.example.finalproject.finalproject.app.myBook.controller.BookController;
import com.example.finalproject.finalproject.app.myBook.entity.MyBook;
import com.example.finalproject.finalproject.app.myBook.service.MyBookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MyBookControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MyBookService myBookService;

    @Test
    @DisplayName("도서 업로드 폼")
    @WithUserDetails("user1")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        get("/book/create")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(BookController.class))
                .andExpect(handler().methodName("showCreate"))
                .andExpect(content().string(containsString("도서 업로드")));
    }

    @Test
    @DisplayName("도서 업로드")
    @WithUserDetails("user1")
    void t2() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/book/create")
                        .param("subject", "제목")
                        .param("Price", "1800")
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(BookController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(redirectedUrlPattern("/book/**"));

        Long songId = Long.valueOf(resultActions.andReturn().getResponse().getRedirectedUrl().replace("/book/", "").split("\\?", 2)[0]);
        assertThat(myBookService.findById(songId).isPresent()).isTrue();
    }
    @Test
    @DisplayName("도서 수정 폼")
    @WithUserDetails("user1")
    void t3() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/book/1/modify"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(BookController.class))
                .andExpect(handler().methodName("showModify"))
                .andExpect(content().string(containsString("도서 수정")));
    }

    @Test
    @DisplayName("도서 수정")
    @WithUserDetails("user1")
    void t4() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/book/1/modify")
                        .param("subject", "제목1 NEW")
                        .param("Price", "1900")
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(BookController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(redirectedUrlPattern("/book/**"));

        Long songId = Long.valueOf(resultActions.andReturn().getResponse().getRedirectedUrl().replace("/book/", "").split("\\?")[0]);

        MyBook myBook = myBookService.findById(songId).get();

        assertThat(myBook).isNotNull();
        assertThat(myBook.getSubject()).isEqualTo("제목1 NEW");
        assertThat(myBook.getPrice()).isEqualTo(1900);

    }
}
