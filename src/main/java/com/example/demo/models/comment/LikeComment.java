package com.example.demo.models.comment;

import org.springframework.data.annotation.Id;

public class LikeComment {
    @Id
    private String id;
    private String idComment;
    private String idUser;
    private long dateCreated;

    public LikeComment() {
        super();
    }

    public LikeComment(String idComment, String idUser, long dateCreated) {
        this.idComment = idComment;
        this.idUser = idUser;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }
}
