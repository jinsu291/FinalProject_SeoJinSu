package com.example.finalproject.finalproject.app.product.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductForm {
    @NotEmpty
    private String subject;
    @NotNull
    private int price;
    @NotNull
    private Long postKeywordId;
    @NotBlank
    private String productTagContents;
}
