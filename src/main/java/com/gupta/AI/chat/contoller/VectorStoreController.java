package com.gupta.AI.chat.contoller;

import com.gupta.AI.chat.beans.VectorStoreRequest;
import com.gupta.AI.chat.service.OllamaService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/store")
public class VectorStoreController {

    @Autowired
    private OllamaService ollamaService;

    @PostMapping("/add")
    public String addText(@RequestBody VectorStoreRequest request) {
        ollamaService.addTextToStore(request.getText());
        return "Content added.";
    }

    @PostMapping("/similaritysearch")
    public List<Document> similaritySearch(@RequestBody VectorStoreRequest request) {
        return ollamaService.similaritySearch(request.getText());
    }

}
