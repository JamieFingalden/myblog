package com.fingalden.blog.service.Impl;

import com.fingalden.blog.domain.User;
import com.fingalden.blog.mapper.UserMapper;
import com.fingalden.blog.service.LoginService;
import com.fingalden.blog.utils.JWTUtil;
import com.fingalden.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    private final JWTUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public LoginServiceImpl(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Result login(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        if (userMapper.Login(user) != null) {
            return Result.success(jwtUtil.createToken(user.getId()));
        }
        return Result.error("用户名或密码错误");
    }

    @Override
    public Result register(User user) {
        int row = userMapper.insert(user);
        if (row > 0) {
            return Result.success(jwtUtil.createToken(user.getId()));
        }
        return Result.error("注册失败");
    }
}
