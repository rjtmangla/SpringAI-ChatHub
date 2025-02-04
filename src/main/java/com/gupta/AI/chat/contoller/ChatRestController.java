package com.gupta.AI.chat.contoller;

import com.gupta.AI.chat.beans.ChatHistoryResponse;
import com.gupta.AI.chat.beans.ChatRequest;
import com.gupta.AI.chat.beans.ChatResponse;
import com.gupta.AI.chat.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private OllamaService ollamaService;

    @GetMapping
    public ChatHistoryResponse getHistory(@RequestParam("conversationId") String id) {
        List<ChatHistoryResponse.ChatHistory> histories = ollamaService.getOldMessages(id);
        ChatHistoryResponse response = new ChatHistoryResponse();
        response.setChatHistories(histories);
        return response;
    }

    @PostMapping
    public ChatResponse getAnswer(@RequestBody ChatRequest request) {
        return ollamaService.getAnswer(request.getConversationId(), request.getMessage());
    }

    @DeleteMapping
    public void removeConversation(@RequestParam("conversationId") String conversationId) {
        ollamaService.removeChatHistory(conversationId);
    }

}
