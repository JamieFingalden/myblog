package com.fingalden.blog.service.Impl;

import com.fingalden.blog.domain.WordBank;
import com.fingalden.blog.mapper.WordBankMapper;
import com.fingalden.blog.service.WordService;
import com.fingalden.blog.utils.JWTUtil;
import com.fingalden.blog.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Service
public class WordServiceImpl implements WordService {
    @Autowired
    private WordBankMapper wordBankMapper;

    @Autowired
    private JWTUtil jwtUtil;

    private Long getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 从请求头中获取 JWT 令牌
        String token = request.getHeader("Authorization");
        Long userId = jwtUtil.getUserId(token);
        return userId;
    }

    @Override
    public Result getWordList() {
        Long userId = getUserId();
        List<WordBank> wordBanks = wordBankMapper.selectAllWorks(userId);
        return Result.success(wordBanks);
    }

    @Override
    public Result addWord(List<WordBank> words) {
        Long userId = getUserId();
        int row = wordBankMapper.insertWords(userId,words);
        if (row > 0) {
            return Result.success(wordBankMapper.selectAllWorks(userId));
        }
        return Result.error("添加失败");
    }
}
