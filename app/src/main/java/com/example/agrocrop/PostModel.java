package com.example.agrocrop;

import java.io.Serializable;

public class PostModel implements Serializable {
    String post_id, post_title,post_content,post_image,post_validation,fullname,username,userid,userimage,no_of_likes,no_of_dislikes,no_of_comment,published_at,user_qualification;


    public PostModel(String post_id, String post_title, String post_content, String post_image, String fullname, String username, String userid, String userimage, String no_of_likes, String no_of_dislikes, String no_of_comment, String published_at, String user_qualification) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.post_image = post_image;
        this.fullname = fullname;
        this.username = username;
        this.userid = userid;
        this.userimage = userimage;
        this.no_of_likes = no_of_likes;
        this.no_of_dislikes = no_of_dislikes;
        this.no_of_comment = no_of_comment;
        this.published_at = published_at;
        this.user_qualification = user_qualification;
    }

    public PostModel() {
    }


    public String getPost_title() {
        return post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public String getPost_image() {
        return post_image;
    }

    public String getPost_validation() {
        return post_validation;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getUserid() {
        return userid;
    }

    public String getUserimage() {
        return userimage;
    }

    public String getNo_of_likes() {
        return no_of_likes;
    }

    public String getNo_of_dislikes() {
        return no_of_dislikes;
    }

    public String getNo_of_comment() {
        return no_of_comment;
    }

    public String getPublished_at() {
        return published_at;
    }

    public String getUser_qualification() {
        return user_qualification;
    }
}
