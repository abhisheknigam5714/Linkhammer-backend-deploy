package com.Link.service;

import com.Link.dtos.ChatResponse;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatbotService {

    private final ChatClient chatClient;

    public ChatResponse chat(String message) {
        try {
            String reply = chatClient.prompt()
                    .user(message)
                    .call()
                    .content();

            return new ChatResponse(reply);

        } catch (Exception e) {
            return new ChatResponse("API request limit exceeded for the free tier. Please upgrade the plan or retry after some time.");
        }
    }
}