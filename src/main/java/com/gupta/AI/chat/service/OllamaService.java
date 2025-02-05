package com.gupta.AI.chat.service;

import com.gupta.AI.chat.beans.ChatHistoryResponse;
import com.gupta.AI.chat.beans.ChatResponse;
import com.gupta.AI.chat.exceptions.InvalidConversionException;
import com.gupta.AI.chat.memory.PersistedChatMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OllamaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OllamaService.class);
    private ChatClient chatClient;
    private PersistedChatMemory chatMemory;
    private VectorStore vectorStore;

    public OllamaService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatMemory = new PersistedChatMemory();
        this.vectorStore = vectorStore;
        this.chatClient = builder.defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory),
                new QuestionAnswerAdvisor(vectorStore)).build();
    }

    public ChatResponse getAnswer(String conversationId, String message) {
        long startTime = System.currentTimeMillis();
        if (StringUtils.isEmpty(conversationId)) {
            //Need to create new conversation Id
            conversationId = createConversationId(message);
        } else {
            validateConversationId(conversationId, "getAnswer");
        }
        String id = conversationId;
        String response = chatClient.prompt().advisors(advisorSpec -> advisorSpec.param(
                        AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, id)).
                user(message).call().content();
        ChatResponse chatResponse = new ChatResponse(conversationId, response);
        LOGGER.info("Got the answer for conversation. conversationId={}, timeTakenToComplete={}", conversationId, (System.currentTimeMillis() - startTime));
        LOGGER.debug("Got the answer for the question. conversationId={}, message={},response={}", conversationId, message, chatResponse);
        return chatResponse;
    }

    private void validateConversationId(String conversationId, String operation) {
        if (conversationId == null || conversationId.isEmpty()) {
            LOGGER.info("Found invalid conversation format in {}. conversionId={}", operation, conversationId);
            throw new InvalidConversionException("Empty conversationId");
        }
        if (!chatMemory.isValidConversationId(conversationId)) {
            LOGGER.info("Found invalid conversation in {}. conversionId={}", operation, conversationId);
            throw new InvalidConversionException("Invalid conversationId");
        }
    }

    public List<ChatHistoryResponse.ChatHistory> getOldMessages(String conversationId) {
        validateConversationId(conversationId, "getHistory");
        List<Message> messages = chatMemory.get(conversationId, 100);
        List<ChatHistoryResponse.ChatHistory> history = createHistoryObject(messages);
        LOGGER.info("Returning history for conversation. ConversationId={}, history={}", conversationId, history);
        return history;
    }

    private List<ChatHistoryResponse.ChatHistory> createHistoryObject(List<Message> messages) {
        List<ChatHistoryResponse.ChatHistory> history = new ArrayList<>();
        for (Message message : messages) {
            if (message.getMessageType() == MessageType.ASSISTANT) {
                AssistantMessage assistantMessage = (AssistantMessage) message;
                history.add(new
                        ChatHistoryResponse.ChatHistory(MessageType.ASSISTANT.getValue(), assistantMessage.getText(),
                        new Date()));
            } else if (message.getMessageType() == MessageType.USER) {
                UserMessage userMessage = (UserMessage) message;
                history.add(new ChatHistoryResponse.ChatHistory(MessageType.USER.getValue(), userMessage.getText(), new Date()));
            }
        }
        return history;
    }


    private String createConversationId(String message) {
        String conversationId = message;
        if (message.length() > 20) {
            conversationId = message.substring(0, 21);
        }
        LOGGER.info("Created a new ConversationId for message. conversationId={}, message={}", conversationId, message);
        return conversationId;
    }

    public void removeChatHistory(String conversationId) {
        validateConversationId(conversationId, "removeChatHistory");
        LOGGER.info("Removed history for conversation. conversationId={}", conversationId);
        chatMemory.clear(conversationId);
    }

    public void addTextToStore(String text) {
        TextSplitter splitter = new TokenTextSplitter();
        Document document = new Document(text);
        vectorStore.add(splitter.split(document));
    }

    public List<Document> similaritySearch(String text) {
        return vectorStore.similaritySearch(text);
    }

    public List<String> getAllConversationIds() {
        LOGGER.info("Got request to fetch all conversationIds");
        return chatMemory.getConversationIds();
    }
}