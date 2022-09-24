package com.example.agrocrop;

import java.io.Serializable;

public class ChartListModel implements Serializable {
    private String userid, username, userimage,Timestamp,lastMessages;
    private String read;

    public ChartListModel(String userid, String username, String userimage, String timestamp, String lastMessages) {
        this.userid = userid;
        this.username = username;
        this.userimage = userimage;
        Timestamp = timestamp;
        this.lastMessages = lastMessages;
    }

    public ChartListModel(String userid, String username, String userimage, String timestamp) {
        this.userid = userid;
        this.username = username;
        this.userimage = userimage;
        Timestamp = timestamp;

    }

    public ChartListModel(String userid, String username, String userimage, String timestamp, String lastMessages, String read) {
        this.userid = userid;
        this.username = username;
        this.userimage = userimage;
        Timestamp = timestamp;
        this.lastMessages = lastMessages;
        this.read = read;
    }

    public ChartListModel() {
    }

    public String isRead() {
        return read;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getUserimage() {
        return userimage;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public String getLastMessages() {
        return lastMessages;
    }

    /*setters*/

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public void setLastMessages(String lastMessages) {
        this.lastMessages = lastMessages;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
