package com.example.finalproject.finalproject.app.product.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductForm {
    @NotBlank
    private String subject;
    @NotNull
    private int price;
}
