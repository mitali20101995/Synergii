package com.example.synergii.models;

public class ChatMessage
{
    private String message;
    private Long timestamp;
    private  String sentBy;
    private String receivedBy;

    public ChatMessage(String message, Long timestamp, String sentBy, String receivedBy) {
        this.message = message;
        this.timestamp = timestamp; //new Date().getTime();
        this.sentBy = sentBy;
        this.receivedBy = receivedBy;
    }

    public ChatMessage(){
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
