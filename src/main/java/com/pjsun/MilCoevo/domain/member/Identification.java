package com.pjsun.MilCoevo.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Slf4j
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Identification {

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 50)
    private String position;

    @Column(length = 50)
    private String nickname;

    /* 생성자 */
    @Builder(builderClassName = "createIdentificationBuilder", builderMethodName = "createIdentificationBuilder")
    Identification(String email, String position, String nickname) {
        Assert.hasText(email, () -> "[Identification] email must not be empty");
        Assert.hasText(position, () -> "[Identification] email must not be empty");
        Assert.hasText(nickname, () -> "[Identification] email must not be empty");

        this.email = email;
        this.position = position;
        this.nickname = nickname;
    }

    /* 비즈니스 로직 */
    public void updatePosition(String position) {
        Assert.hasText(position, () -> "[Identification] position must not be empty");

        this.position = position;
    }

    public void updateNickname(String nickname) {
        Assert.hasText(nickname, () -> "[Identification] nickname must not be empty");

        this.nickname = nickname;
    }
}
