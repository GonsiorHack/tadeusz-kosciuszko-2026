package com.github.gonsior.kosciuszkon.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CyberFactController {
    private final ChatClient chatClient;

    public CyberFactController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/fact")
    public String getFact(@RequestParam(defaultValue = "podstawach szyfrowania") String topic) {
        String systemPrompt = String.format(
                "Wciel się w rolę fascynującego historyka i nadwornego maga tajemnic. " +
                        "Twoim zadaniem jest opowiedzieć jedną niezwykle ciekawą, mało znaną anegdotę z historii kryptografii na temat: %s. " +
                        "ZASADY: " +
                        "1. CAŁKOWITY ZAKAZ wspominania o internecie, komputerach, AES, kłódkach czy sejfach. " +
                        "2. Skup się na prawdziwych historiach: starożytność, wojny, Enigma, szpiedzy, fizyczne maszyny szyfrujące, niezwykłe metody ukrywania wiadomości. " +
                        "3. Pisz obrazowo, jakbyś opowiadał tajemniczą legendę. Używaj prostego języka. " +
                        "4. Zwracaj się bezpośrednio do słuchacza (np. 'Czy wiesz, że...', 'Wyobraź sobie...'). Maksymalnie 3 zdania. " +
                        "5. ZWRÓĆ WYŁĄCZNIE SAM TEKST CIEKAWOSTKI. Zero formatowania markdown (bez gwiazdek i pogrubień), zero powitań.",
                topic
        );

        return chatClient.prompt()
                .user(systemPrompt)
                .call()
                .content();
    }
}
