package com.gupta.AI.chat.beans;

import java.util.Date;
import java.util.List;

public class ChatHistoryResponse {

    private List<ChatHistory> chatHistories;

    public List<ChatHistory> getChatHistories() {
        return chatHistories;
    }

    public void setChatHistories(List<ChatHistory> chatHistories) {
        this.chatHistories = chatHistories;
    }

    public static class ChatHistory {
        private final String caller;
        private final String message;

        private final Date date;

        public ChatHistory(String caller, String message, Date date) {
            this.caller = caller;
            this.message = message;
            this.date = date;
        }

        public String getCaller() {
            return caller;
        }

        public String getMessage() {
            return message;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public String toString() {
            return "ChatHistory{" +
                    "caller='" + caller + '\'' +
                    ", message='" + message + '\'' +
                    ", date=" + date +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ChatHistoryResponse{" +
                "chatHistories=" + chatHistories +
                '}';
    }
}
