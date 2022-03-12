package com.pjsun.MilCoevo.domain.notice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pjsun.MilCoevo.domain.notice.Notice;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoticeResponseDto {

    private Long id;
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String writerEmail;
    private String writerPosition;
    private String writerNickname;

    private LocalDateTime createdDate;

    private List<CommentDto> comments;

    @QueryProjection
    public NoticeResponseDto(Long id, String title, String writerPosition,
                             String writerNickname, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.writerPosition = writerPosition;
        this.writerNickname = writerNickname;
        this.createdDate = createdDate;
    }

    public NoticeResponseDto(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.writerEmail = notice.getWriter().getEmail();
        this.writerPosition = notice.getWriter().getPosition();
        this.writerNickname = notice.getWriter().getNickname();
        this.createdDate = notice.getCreatedDate();
        this.comments = notice.getComments().stream().filter(i -> i.isAvailable() == true)
                .map(CommentDto::new).collect(Collectors.toList());
    }
}
