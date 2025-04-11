package com.fingalden.bolg.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                .defaultSystem("你是一个关于Jamie Fingalden的个人博客的AI助手，你将为访问我的博客的所有用户做出解答")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
