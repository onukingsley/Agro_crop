package com.example.agrocrop;

import java.io.Serializable;

public class NewsModel implements Serializable {
    String Post_title, Post_description, Post_url, Post_image, Post_content, Post_author,Publisheddate;

    public NewsModel(String post_title, String post_description, String post_url, String post_image, String post_content, String post_author, String publisheddate) {
        Post_title = post_title;
        Post_description = post_description;
        Post_url = post_url;
        Post_image = post_image;
        Post_content = post_content;
        Post_author = post_author;
        Publisheddate = publisheddate;
    }

    public NewsModel() {
    }

    public String getPost_title() {
        return Post_title;
    }

    public String getPost_description() {
        return Post_description;
    }

    public String getPost_url() {
        return Post_url;
    }

    public String getPost_image() {
        return Post_image;
    }

    public String getPost_content() {
        return Post_content;
    }

    public String getPost_author() {
        return Post_author;
    }

    public String getPublisheddate() {
        return Publisheddate;
    }
}
