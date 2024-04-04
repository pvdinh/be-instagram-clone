package com.example.demo.models.comment;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ReplyComment {
    @Id
    private String id;
    private String content;
    private String idPost;
    private String idUser;
    private String idComment;
    private long dateCommented;

    public ReplyComment() {
        super();
    }

    public ReplyComment(String content, String idPost, String idUser, String idComment, long dateCommented) {
        this.content = content;
        this.idPost = idPost;
        this.idUser = idUser;
        this.idComment = idComment;
        this.dateCommented = dateCommented;
    }

    public ReplyComment(String id, String content, String idPost, String idUser, String idComment, long dateCommented) {
        this.id = id;
        this.content = content;
        this.idPost = idPost;
        this.idUser = idUser;
        this.idComment = idComment;
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

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }
}
