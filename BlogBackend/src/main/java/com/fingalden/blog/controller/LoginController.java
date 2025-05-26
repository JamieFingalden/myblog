package com.fingalden.blog.controller;

import com.fingalden.blog.domain.User;
import com.fingalden.blog.service.LoginService;
import com.fingalden.blog.service.PostsService;
import com.fingalden.blog.utils.JWTUtil;
import com.fingalden.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final JWTUtil jwtUtil;

    @Autowired
    public LoginController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    private LoginService loginService;

    @Autowired
    private PostsService postsService;

    @GetMapping("/list")
    public Result getPostList() {
        System.out.println("list");
        return Result.success(postsService.getPostsList());
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (Objects.equals(user.getUsername(), "游客")) {
            String token = jwtUtil.createToken(user.getId());
            return Result.success(token);
        }
        return loginService.login(user);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return loginService.register(user);
    }
}
