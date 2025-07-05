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
////
////https://ai.gopubby.com/microsofts-graphrag-autogen-ollama-chainlit-fully-local-free-multi-agent-rag-superbot-61ad3759f06f
////
////        https://tpbabparn.medium.com/in-house-llm-application-by-spring-ai-ollama-91c48e2d2d38
////
////        https://levelup.gitconnected.com/add-generative-ai-in-your-spring-boot-application-spring-ai-12561b1adf08
////        https://yogeshbali.medium.com/exploring-spring-ai-in-the-spring-boot-3-0-x-6c12ee5e7930
////        https://medium.com/@huzmedianyc/building-ai-powered-applications-with-deepseek-and-spring-ai-3b2996d1de3d
////
////        https://medium.com/software-newspaper/building-a-document-query-system-with-spring-ai-pgvector-and-ollama-dfafccad26c2
////        https://wire.insiderfinance.io/real-time-ai-stock-advisor-with-ollama-streamlit-c8ce727c236f
////
////        https://ai.gopubby.com/run-llama-3-on-your-laptop-an-introduction-to-ollama-for-beginners-06ba8a110d12
////        https://foojay.io/today/spring-ai-how-to-write-genai-applications-with-java/?ref=dailydev
////
//
//
//https://github.com/mckaywrigley/chatbot-ui
//        https://github.com/ivanfioravanti/chatbot-ollama
//        https://github.com/rjtmangla/E-shop/blob/master/src/main/resources/templates/admin.html
//        https://github.com/rjtmangla/springboot-notes-app
//
//        https://github.com/rjtmangla/StickyNotes
//        https://github.com/rjtmangla/Ecommerce-Website-Spring-Boot
//        https://github.com/rjtmangla/Books_notes
//        https://github.com/rjtmangla/interview-notes1
