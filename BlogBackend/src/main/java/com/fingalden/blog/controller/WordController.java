package com.fingalden.blog.controller;


import com.fingalden.blog.domain.WordBank;
import com.fingalden.blog.service.WordService;
import com.fingalden.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/word")
public class WordController {
    @Autowired
    private WordService wordService;

    @GetMapping("/getAllWords")
    public Result getAllWords() {
        return wordService.getWordList();
    }

    @PostMapping("/addWord")
    public Result addWord(@RequestBody List<WordBank> words) {
        return wordService.addWord(words);
    }
}
