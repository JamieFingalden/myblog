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
        // 获取请求的主机名和端口号
        String host = request.getHeader("referer");
        System.out.println("请求的主机头: " + host);
        String token = request.getHeader("Authorization");
//        if (token == null || jwtUtil.isExpiration(token.split(" ")[1])) {
        if (token == null || jwtUtil.isExpiration(token)) {
            // 设置响应的内容类型为 JSON
            response.setContentType("application/json;charset=UTF-8");
            // 将错误信息写入响应体
            response.getWriter().write(Result.error("token is null or expired").toString());
            Result.error("token is null or expired");
            return false; // 阻止请求继续执行
        } else {
            return true; // 允许请求继续执行
        }
    }
}