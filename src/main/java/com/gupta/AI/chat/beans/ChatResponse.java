package com.gupta.AI.chat.beans;

public class ChatResponse {
    private final String conversationId;
    private final String response;

    public ChatResponse(String conversationId, String response) {
        this.conversationId = conversationId;
        this.response = response;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "ChatResponse{" +
                "conversationId='" + conversationId + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
