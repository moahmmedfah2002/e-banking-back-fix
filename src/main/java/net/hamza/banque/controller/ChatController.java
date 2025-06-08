package net.hamza.banque.controller;

import lombok.RequiredArgsConstructor;
import net.hamza.banque.dto.ChatResponseDto;
import net.hamza.banque.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gemini")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor

public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDto> chat(@RequestParam String request) {
        try {
            String response = chatService.chat(request);
            return ResponseEntity.ok(new ChatResponseDto(response));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ChatResponseDto("Error: " + e.getMessage()));
        }
    }


}
