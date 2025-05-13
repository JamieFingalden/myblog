package com.fingalden.blog.interceptor;

import com.fingalden.blog.utils.JWTUtil;
import com.fingalden.blog.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || jwtUtil.isExpiration(token)) {
            Result.error("token is null or expired");
            return false;
        }else {
            return true;
        }
    }
}