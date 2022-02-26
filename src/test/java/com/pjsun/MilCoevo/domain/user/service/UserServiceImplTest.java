package com.pjsun.MilCoevo.domain.user.service;

import com.pjsun.MilCoevo.domain.user.Role;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.domain.user.repository.UserRepository;
import com.pjsun.MilCoevo.exception.DuplicateUserException;
import com.pjsun.MilCoevo.test.MockTest;
import com.pjsun.MilCoevo.test.builder.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class UserServiceImplTest extends MockTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void registerSuccessTest() {
        //given
        User user = UserBuilder.build(1L);

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(user.getPassword())).willReturn(user.getPassword());
        given(userRepository.save(any())).willReturn(user);

        //when
        Long id = userService.register(user.getEmail(), user.getPassword());

        //then
        assertThat(id).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("회원가입 중복 실패")
    void registerDuplicateTest() {
        //given
        User user = UserBuilder.build(1L);

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        //then
        assertThatThrownBy(() -> userService.register(user.getEmail(), user.getPassword()))
                .isInstanceOf(DuplicateUserException.class);
    }
}