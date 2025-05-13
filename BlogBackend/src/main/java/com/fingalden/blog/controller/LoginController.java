package com.fingalden.blog.controller;

import com.fingalden.blog.domain.User;
import com.fingalden.blog.utils.JWTUtil;
import com.fingalden.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final JWTUtil jwtUtil;

    @Autowired
    public LoginController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public Result login(@RequestBody User user) {
        user = new User(1L, "admin", "123456");
        String token = jwtUtil.createToken(user.getId());
        return Result.success(token);
    }
}
