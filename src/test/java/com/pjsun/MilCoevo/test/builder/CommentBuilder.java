package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.notice.Comment;

public class CommentBuilder {

    public static Comment build(Long id) {
        return new Comment(1L, "content", true,
                IdentificationBuilder.build(), null);
    }
}
