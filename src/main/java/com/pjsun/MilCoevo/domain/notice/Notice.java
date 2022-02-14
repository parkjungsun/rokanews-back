package com.pjsun.MilCoevo.domain.notice;

import com.pjsun.MilCoevo.domain.BaseEntity;
import com.pjsun.MilCoevo.domain.BooleanToYNConverter;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.member.Identification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Slf4j
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notices")
@ToString(exclude = {"group", "comments"})
public class Notice extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "notice_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

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
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /* 연관관계 메서드 */
    public void setGroup(Group group) {
        this.group = group;
        group.getNotices().add(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setNotice(this);
    }

    /* 생성자 */
    @Builder(builderClassName = "of", builderMethodName = "of")
    private Notice(String title, String content, Identification writer,
                    boolean isAvailable, Group group) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.isAvailable = isAvailable;

        if(!ObjectUtils.isEmpty(group)) {
            setGroup(group);
        }
    }

    @Builder(builderClassName = "createNoticeBuilder", buildMethodName = "createNoticeBuilder")
    public static Notice createNotice(String title, String content,
                                      Identification writer, Group group) {
        Assert.hasText(title, () -> "[Notice] title must not be empty");
        Assert.notNull(content, () -> "[Notice] content must not be null");
        Assert.notNull(writer, () -> "[Notice] writer must not be null");

        return Notice.of()
                .title(title).content(content)
                .writer(writer).isAvailable(true)
                .group(group)
                .build();
    }

    /* 비즈니스 로직 */
    public void remove() {
        this.isAvailable = false;
    }

    public void updateTitleAndContent(String title, String content) {
        Assert.hasText(title, () -> "[Notice] title must not be empty");
        Assert.notNull(content, () -> "[Notice] content must not be null");

        this.title = title;
        this.content = content;
    }

}
