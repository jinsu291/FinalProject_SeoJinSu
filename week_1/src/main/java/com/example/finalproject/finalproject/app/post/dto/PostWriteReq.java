package com.example.finalproject.finalproject.app.post.dto;

import com.example.finalproject.finalproject.app.member.entity.Member;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.FetchType.LAZY;

@Data
public class PostWriteReq {

    private String subject;

    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @Column(columnDefinition = "LONGTEXT")
    private String contentHtml;

    @ManyToOne(fetch = LAZY)
    private Member author;

    public String getForPrintContentHtml() {
        return contentHtml.replaceAll("toastui-editor-ww-code-block-highlighting", "");
    }

}