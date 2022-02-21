package com.pjsun.MilCoevo.domain.user.service;

import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.exception.DuplicateUserException;
import com.pjsun.MilCoevo.domain.user.repository.UserRepository;
import com.pjsun.MilCoevo.exception.InvalidTokenException;
import com.pjsun.MilCoevo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long register(String email, String password) throws DuplicateUserException {
        if (userRepository.findByEmail(email).orElse(null) != null) {
            throw new DuplicateUserException("Email Already Registered");
        }

        User user = User.createByUserBuilder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        return userRepository.save(user).getId();
    }

    // 유저 네임에 해당하는 거 가져오는거
    // public Optional<User> getUserWithAuthorities(String email) { return userRepository.findByEmail(email); }

    // 현재 SpringSecurity Context에 저장된 것만 가져올 수 있음
    public User getUserFromContext() {
        return SecurityUtil.getCurrentEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new InvalidTokenException("Token Owner is Invalid"));
    }
}
