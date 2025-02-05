package com.gupta.AI.chat.contoller;

import com.gupta.AI.chat.beans.ChatHistoryResponse;
import com.gupta.AI.chat.beans.ChatRequest;
import com.gupta.AI.chat.beans.ChatResponse;
import com.gupta.AI.chat.beans.ConversationListResponse;
import com.gupta.AI.chat.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/chat", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/allconverstions")
    public ConversationListResponse getAllConversation() {
        List<String> conversationIds = ollamaService.getAllConversationIds();
        return new ConversationListResponse(conversationIds);
    }

}
