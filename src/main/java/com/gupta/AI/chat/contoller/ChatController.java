package com.gupta.AI.chat.contoller;

import com.gupta.AI.chat.beans.ChatResponse;
import com.gupta.AI.chat.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@Controller
public class ChatController {

    @Autowired
    private OllamaService ollamaService;

    @PostMapping("/question")
    public String getAnswer(@RequestParam("question") String question,
//                            @RequestParam("conversationId")
    String conversationId, Model model) {
        conversationId="11";
        ChatResponse response = ollamaService.getAnswer(conversationId, question);
        model.addAttribute("answer", response.getResponse());
        model.addAttribute("conversationId", response.getConversationId());
        return "index";
    }


}
