package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.user.Role;
import com.pjsun.MilCoevo.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserBuilder {

    public static User build(Long id) {
        return new User(id, "test@test.com", "password",
                Role.ROLE_USER, true,
                LocalDateTime.now(), new ArrayList<>());
    }
}
