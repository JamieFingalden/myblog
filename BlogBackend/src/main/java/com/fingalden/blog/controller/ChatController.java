package com.fingalden.blog.controller;

import com.fingalden.blog.domain.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("/AIChat")
public class ChatController {
    private final ChatClient chatClient;

    // 修改控制器方法
    @PostMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatRequest request){
        return chatClient
                .prompt()
                .user(request.getPrompt())
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, request.getChatId()))
                .stream()
                .content();
    }
}
