package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.member.Identification;

public class IdentificationBuilder {

    public static Identification build() {
        return Identification.createIdentificationBuilder()
                .email("test@email.com").position("position")
                .nickname("nickname").build();
    }
}
