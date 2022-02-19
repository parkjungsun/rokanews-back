package com.pjsun.MilCoevo.domain.notice.dto;

import com.pjsun.MilCoevo.domain.notice.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    private String email;
    private String position;
    private String nickname;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.email = comment.getWriter().getEmail();
        this.position = comment.getWriter().getPosition();
        this.nickname = comment.getWriter().getNickname();
    }
}
