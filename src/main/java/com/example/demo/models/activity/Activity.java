package com.example.demo.models.activity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Activity {
    @Id
    private String id;
    private String idCurrentUser;
    private String idInteractUser;
    private String idPost;
    private String typeActivity;
    private int status;
    private double dateActivity;

    public Activity() {
        super();
    }

    public Activity(String id, String idCurrentUser, String idInteractUser, String idPost, String typeActivity, int status, double dateActivity) {
        this.id = id;
        this.idCurrentUser = idCurrentUser;
        this.idInteractUser = idInteractUser;
        this.idPost = idPost;
        this.typeActivity = typeActivity;
        this.status = status;
        this.dateActivity = dateActivity;
    }

    public Activity(String idCurrentUser, String idInteractUser, String idPost, String typeActivity, int status, double dateActivity) {
        this.idCurrentUser = idCurrentUser;
        this.idInteractUser = idInteractUser;
        this.idPost = idPost;
        this.typeActivity = typeActivity;
        this.status = status;
        this.dateActivity = dateActivity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCurrentUser() {
        return idCurrentUser;
    }

    public void setIdCurrentUser(String idCurrentUser) {
        this.idCurrentUser = idCurrentUser;
    }

    public String getIdInteractUser() {
        return idInteractUser;
    }

    public void setIdInteractUser(String idInteractUser) {
        this.idInteractUser = idInteractUser;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getTypeActivity() {
        return typeActivity;
    }

    public void setTypeActivity(String typeActivity) {
        this.typeActivity = typeActivity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getDateActivity() {
        return dateActivity;
    }

    public void setDateActivity(double dateActivity) {
        this.dateActivity = dateActivity;
    }
}
