package com.github.gonsior.kosciuszkon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CyberFactController {
    private final ChatClient chatClient;

    public CyberFactController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/fact")
    public String getFact() {
        return chatClient.prompt()
                .user("Podaj ciekawostkę o cyberbezpieczeństwie")
                .call()
                .content();
    }
}
