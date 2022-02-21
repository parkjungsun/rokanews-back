package com.pjsun.MilCoevo.domain.user.service;

import com.pjsun.MilCoevo.domain.user.User;

public interface UserService {
    Long register(String email, String password);
    User getUserFromContext();
}
