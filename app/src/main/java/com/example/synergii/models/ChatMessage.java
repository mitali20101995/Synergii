package com.example.synergii.models;

public class ChatMessage
{
    private String[] message;
    private User userId;
    private String timestamp;
    private User profilePhoto;
    private String name;

    public ChatMessage(String[] message, User userId, String timestamp, User profilePhoto, String name) {
        this.message = message;
        this.userId = userId;
        this.timestamp = timestamp;
        this.profilePhoto = profilePhoto;
        this.name = name;
    }

    public ChatMessage(){

}

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public User getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(User profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
