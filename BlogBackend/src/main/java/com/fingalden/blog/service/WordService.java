package com.fingalden.blog.service;

import com.fingalden.blog.domain.WordBank;
import com.fingalden.blog.utils.Result;

import java.util.List;

public interface WordService {
    Result getWordList();

    Result addWord(List<WordBank> words);
}
