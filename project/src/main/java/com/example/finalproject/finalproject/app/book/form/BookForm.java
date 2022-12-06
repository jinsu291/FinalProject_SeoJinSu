package com.example.finalproject.finalproject.app.book.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BookForm {
    @NotBlank
    private String subject;
    @NotNull
    private int price;
}
