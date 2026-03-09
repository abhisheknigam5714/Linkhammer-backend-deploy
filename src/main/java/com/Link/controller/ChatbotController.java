package com.Link.controller;

import com.Link.dtos.ChatRequest;
import com.Link.dtos.ChatResponse;
import com.Link.service.ChatbotService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatbotController {

    private ChatbotService chatbotService;

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        ChatResponse response = chatbotService.chat(chatRequest.getMessage());
        return ResponseEntity.ok(response);
    }
}