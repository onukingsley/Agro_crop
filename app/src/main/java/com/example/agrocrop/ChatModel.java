package com.example.agrocrop;

public class ChatModel {
    String timestamp, message, userimage, username, userid;

    public ChatModel() {
    }

    public ChatModel(String timestamp, String message, String userimage, String username, String userid) {
        this.timestamp = timestamp;
        this.message = message;
        this.userimage = userimage;
        this.username = username;
        this.userid = userid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getUserimage() {
        return userimage;
    }

    public String getUsername() {
        return username;
    }

    public String getUserid() {
        return userid;
    }
}
