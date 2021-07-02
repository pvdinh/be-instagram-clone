package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Like {
    @Id
    private String id;
    private String idPost;
    private String idUser;
    private long dateLiked;

    public Like() {
        super();
    }

    public Like(String idPost, String idUser, long dateLiked) {
        this.idPost = idPost;
        this.idUser = idUser;
        this.dateLiked = dateLiked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getDateLiked() {
        return dateLiked;
    }

    public void setDateLiked(long dateLiked) {
        this.dateLiked = dateLiked;
    }
}
