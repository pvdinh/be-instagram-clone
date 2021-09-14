package com.example.demo.models.home;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Story {
    @Id
    private String id;
    private String idPost;
    private String idUser;
    private long dateStart;
    private long dateEnd;

    public Story() {
        super();
    }

    public Story(String id, String idPost, String idUser, long dateStart, long dateEnd) {
        this.id = id;
        this.idPost = idPost;
        this.idUser = idUser;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
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

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }
}
