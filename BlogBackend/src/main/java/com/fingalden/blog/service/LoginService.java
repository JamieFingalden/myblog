package com.fingalden.blog.service;

import com.fingalden.blog.domain.User;
import com.fingalden.blog.utils.Result;

public interface LoginService {
    Result login(User user);
    Result register(User user);
}
