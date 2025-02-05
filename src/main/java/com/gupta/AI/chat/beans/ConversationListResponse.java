package com.gupta.AI.chat.beans;

import java.util.List;

public class ConversationListResponse {
    private List<String> conversationIds;

    public ConversationListResponse(List<String> conversationIds) {
        this.conversationIds = conversationIds;
    }

    public List<String> getConversationIds() {
        return conversationIds;
    }

    public void setConversationIds(List<String> conversationIds) {
        this.conversationIds = conversationIds;
    }
}
