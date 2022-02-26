package com.pjsun.MilCoevo.domain.member;

import com.pjsun.MilCoevo.test.DomainTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentificationTest extends DomainTest {

    @Test
    @DisplayName("ID 생성")
    void createIdentification() {
        //given
        String email = "test@test.com";
        String position = "position";
        String nickname = "nickname";

        //when
        Identification info = Identification.createIdentificationBuilder()
                .email(email).position(position).nickname(nickname).build();

        //then
        assertThat(info.getEmail()).isEqualTo(email);
        assertThat(info.getNickname()).isEqualTo(nickname);
        assertThat(info.getPosition()).isEqualTo(position);
    }

    @Test
    @DisplayName("비즈니스 로직")
    void businessTest() {
        //given
        String email = "test@test.com";
        String position = "position";
        String nickname = "nickname";
        String updatePosition = "updatePosition";
        String updateNickname = "updateNickname";

        Identification info = Identification.createIdentificationBuilder()
                .email(email).position(position).nickname(nickname).build();

        //when
        info.updatePosition(updatePosition);
        info.updateNickname(updateNickname);

        //then
        assertThat(info.getNickname()).isEqualTo(updateNickname);
        assertThat(info.getPosition()).isEqualTo(updatePosition);
    }
}