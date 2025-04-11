package com.fingalden.bolg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("/AIChat")
public class ChatController {
    private final ChatClient chatClient;

    @RequestMapping("/chat")
    public Flux<String> chat(String prompt){
        return chatClient
                .prompt()
                .user(prompt)
                .stream()
                .content();
    }
}
