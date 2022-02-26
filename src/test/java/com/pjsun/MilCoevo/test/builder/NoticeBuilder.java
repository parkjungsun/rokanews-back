package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.notice.Notice;

import java.util.ArrayList;

public class NoticeBuilder {

    public static Notice build(Long id) {
        return new Notice(1L, "title", "content",
                true, IdentificationBuilder.build(), null, new ArrayList<>());
    }
}
