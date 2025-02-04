package com.gupta.AI.chat;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }



    @Bean
    public ApplicationRunner applicationRunner(VectorStore vectorStore) {
        return args -> {
            PagePdfDocumentReader reader = new PagePdfDocumentReader("classpath:/3.pdf");
            List<Document> documents = reader.get();
            vectorStore.add(documents);
        };
    }
}
