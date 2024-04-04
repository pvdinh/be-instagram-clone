package com.example.demo.models.group;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class GroupMember {
    @Id
    private String id;
    private String idGroup;
    private String idUser;
    private int status;
    private String role;
    private String idUserInvite;
    private long dateJoined;
    private long dateCreated;

    public GroupMember() {
        super();
    }

    public GroupMember(String idGroup, String idUser, int status, String role, String idUserInvite, long dateJoined, long dateCreated) {
        this.idGroup = idGroup;
        this.idUser = idUser;
        this.status = status;
        this.role = role;
        this.idUserInvite = idUserInvite;
        this.dateJoined = dateJoined;
        this.dateCreated = dateCreated;
    }

    public String getIdUserInvite() {
        return idUserInvite;
    }

    public void setIdUserInvite(String idUserInvite) {
        this.idUserInvite = idUserInvite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(long dateJoined) {
        this.dateJoined = dateJoined;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }
}
