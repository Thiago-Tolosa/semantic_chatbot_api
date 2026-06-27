package com.thiago.knnchatbotapi.controllers;

import com.thiago.knnchatbotapi.dto.ChatRequest;
import com.thiago.knnchatbotapi.dto.ChatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        ChatResponse response = new ChatResponse();

        response.setAnswer("Recebi: "+request.getMessage());

        return response;
    }
}
