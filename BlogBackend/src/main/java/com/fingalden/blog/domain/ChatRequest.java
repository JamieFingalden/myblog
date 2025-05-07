package com.fingalden.blog.domain;

public class ChatRequest {
    private String prompt;
    private String chatId;

    public ChatRequest() {
    }

    public ChatRequest(String prompt, String chatId) {
        this.prompt = prompt;
        this.chatId = chatId;
    }

    // getter和setter
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
