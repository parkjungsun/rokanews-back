package com.pjsun.MilCoevo.domain.user;

import com.pjsun.MilCoevo.test.DomainTest;
import org.h2.engine.Domain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest extends DomainTest {

    @Test
    @DisplayName("일반 사용자 생성")
    void createByUserTest() {
        //given
        String email = "test@test.com";
        String password = "password";

        //when
        User user = User.createByUserBuilder()
                .email(email).password(password).build();

        //then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRole()).isEqualTo(Role.ROLE_USER);
        assertThat(user.isAvailable()).isEqualTo(true);
    }

    @Test
    @DisplayName("관리자 생성")
    void createByAdminTest() {
        //given
        String email = "test@test.com";
        String password = "password";

        //when
        User user = User.createByAdminBuilder()
                .email(email).password(password).build();

        //then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(user.isAvailable()).isEqualTo(true);
    }

    @Test
    @DisplayName("비즈니스 로직")
    void businessTest() {
        //given
        String email = "test@test.com";
        String password = "password";
        User user = User.createByUserBuilder()
                .email(email).password(password).build();

        //when
        user.recordLastLoginDate();
        user.changeAvailability(false);
        user.changeRole(Role.ROLE_ADMIN);

        //then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(user.isAvailable()).isEqualTo(false);
        assertThat(user.getLastLoginDate()).isNotNull();
    }
}