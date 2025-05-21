package com.fingalden.blog.service.Impl;

import com.fingalden.blog.domain.User;
import com.fingalden.blog.mapper.UserMapper;
import com.fingalden.blog.service.LoginService;
import com.fingalden.blog.utils.JWTUtil;
import com.fingalden.blog.utils.MD5Util;
import com.fingalden.blog.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final JWTUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;


    @Autowired
    public LoginServiceImpl(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Result login(User user) {
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        User user1 = userMapper.Login(user);
        if (user1 != null) {
            log.info("用户名：{}，密码：{}", user.getUsername(), user.getPassword());
            return Result.success(jwtUtil.createToken(user1.getId()));
        }
        return Result.error("用户名或密码错误");
    }

    @Override
    public Result register(User user) {
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        int row = userMapper.insert(user);
        if (row > 0) {
            return Result.success(jwtUtil.createToken(user.getId()));
        }
        return Result.error("注册失败");
    }
}
