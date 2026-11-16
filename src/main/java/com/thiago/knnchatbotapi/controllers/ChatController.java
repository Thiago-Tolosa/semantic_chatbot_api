package com.thiago.knnchatbotapi.controllers;

import com.thiago.knnchatbotapi.dto.ChatRequest;
import com.thiago.knnchatbotapi.dto.ChatResponse;
import com.thiago.knnchatbotapi.services.ChatServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatServices chatServices;

    public ChatController( ChatServices chatServices ){
        this.chatServices = chatServices;
    }
    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        ChatResponse response = new ChatResponse();

        response.setAnswer(chatServices.answer(request.getMessage()));

        return response;
    }
}
