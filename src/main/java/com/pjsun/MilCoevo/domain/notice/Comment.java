package com.pjsun.MilCoevo.domain.notice;

import com.pjsun.MilCoevo.domain.BaseEntity;
import com.pjsun.MilCoevo.domain.BooleanToYNConverter;
import com.pjsun.MilCoevo.domain.member.Identification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Slf4j
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
@ToString(exclude = {"notice"})
public class Comment extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(length = 4096)
    private String content;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(length = 1)
    private boolean isAvailable;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "writer_email", nullable = false)),
            @AttributeOverride(name = "position", column = @Column(name = "writer_position", nullable = false)),
            @AttributeOverride(name = "nickname", column = @Column(name = "writer_nickname", nullable = false))
    })
    private Identification writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    /* 연관관계 메서드 */
    public void setNotice(Notice notice) {
        this.notice = notice;
        notice.getComments().add(this);
    }

    /* 생성자 */
    @Builder(builderClassName = "of", builderMethodName = "of")
    private Comment(String content, Identification writer,
                    boolean isAvailable, Notice notice) {
        this.content = content;
        this.writer = writer;
        this.isAvailable = isAvailable;

        if(!ObjectUtils.isEmpty(notice)) {
            setNotice(notice);
        }
    }

    @Builder(builderClassName = "createCommentBuilder", builderMethodName = "createCommentBuilder")
    public static Comment createComment(String content, Identification writer, Notice notice) {
        Assert.hasText(content, () -> "[Comment] content must not be empty");
        Assert.notNull(writer, () -> "[Comment] writer must not be null");

        return Comment.of()
                .content(content).writer(writer)
                .isAvailable(true).notice(notice)
                .build();
    }

    /* 비즈니스 로직 */
    public void remove() {
        this.isAvailable = false;
    }

}
