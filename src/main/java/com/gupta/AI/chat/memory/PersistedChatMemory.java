package com.gupta.AI.chat.memory;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersistedChatMemory implements ChatMemory {

    private final Map<String, List<Message>> conversationHistory = new ConcurrentHashMap<>();
    private final List<String> conversationIds = new ArrayList<>();

    @Override
    public void add(String conversationId, List<Message> messages) {
        this.conversationHistory.putIfAbsent(conversationId, new ArrayList<>());
        if (!conversationIds.contains(conversationId)) {
            conversationIds.add(conversationId);
        }
        this.conversationHistory.get(conversationId).addAll(messages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> all = this.conversationHistory.get(conversationId);
        return all != null ? all.stream().skip(Math.max(0, all.size() - lastN)).toList() : List.of();
    }

    @Override
    public void clear(String conversationId) {
        this.conversationHistory.remove(conversationId);
        this.conversationIds.remove(conversationId);
    }


    public List<String> getConversationIds() {
        return conversationIds;
    }

    public boolean isValidConversationId(String conversationId) {
        return conversationHistory.containsKey(conversationId);
    }
}
