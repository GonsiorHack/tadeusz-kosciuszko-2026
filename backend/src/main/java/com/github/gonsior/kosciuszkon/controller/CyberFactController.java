package com.github.gonsior.kosciuszkon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CyberFactController {
    private final ChatClient chatClient;

    @GetMapping("/fact")
    public String getFact() {
        return chatClient.prompt()
                .user("Podaj ciekawostkę o cyberbezpieczeństwie")
                .call()
                .content();
    }
}
