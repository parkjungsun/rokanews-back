package com.pjsun.MilCoevo.domain.user.repository;

import com.pjsun.MilCoevo.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user;
    String email = "userTest@test.com";
    String password = "password";

    @BeforeEach
    void init() {
        user = User.createByUserBuilder()
                .email(email).password(password)
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("회원 저장 확인")
    void userSave() {
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("이메일으로 유저 검색_존재")
    void findByEmailTest_Exist() {
        User result = userRepository.findByEmail(email).orElse(null);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("이메밀으로 유저 검색_부재")
    void findByEmailTest_NoExist() {
        Optional<User> result = userRepository.findByEmail("Anything");

        assertThatThrownBy(result::get)
                .isInstanceOf(NoSuchElementException.class);
    }
}