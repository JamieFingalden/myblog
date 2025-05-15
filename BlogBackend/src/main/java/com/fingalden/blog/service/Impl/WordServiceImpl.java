package com.fingalden.blog.service.Impl;

import com.fingalden.blog.domain.WordBank;
import com.fingalden.blog.mapper.WordBankMapper;
import com.fingalden.blog.service.WordService;
import com.fingalden.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordServiceImpl implements WordService {
    @Autowired
    private WordBankMapper wordBankMapper;

    @Override
    public Result getWordList() {
        List<WordBank> wordBanks = wordBankMapper.selectAllWorks();
        System.out.println(wordBanks);
        return Result.success(wordBanks);
    }

    @Override
    public Result addWord(List<WordBank> words) {
        int row = wordBankMapper.insertWords(words);
        if (row > 0) {
            return Result.success(wordBankMapper.selectAllWorks());
        }
        return Result.error("添加失败");
    }
}
