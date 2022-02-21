package com.example.demo.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Comment {
    private String id;
    private String content;
    private String idPost;
    private String idUser;
    private long dateCommented;

    public Comment() {
        super();
    }

    public Comment(String id, String content, String idPost, String idUser, long dateCommented) {
        this.id = id;
        this.content = content;
        this.idPost = idPost;
        this.idUser = idUser;
        this.dateCommented = dateCommented;
    }   

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public long getDateCommented() {
        return dateCommented;
    }

    public void setDateCommented(long dateCommented) {
        this.dateCommented = dateCommented;
    }
}
