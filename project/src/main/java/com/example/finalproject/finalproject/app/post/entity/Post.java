package com.example.finalproject.finalproject.app.post.entity;

import com.example.finalproject.finalproject.app.base.entity.BaseEntity;
import com.example.finalproject.finalproject.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Post extends BaseEntity {

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

    public String getJdenticon() {
        return "post__" + getId();
    }
}