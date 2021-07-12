package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndex(name = "userCurrent_userFollowing",def = "{'userCurrent' : 1, 'userFollowing' : 1}", unique = true)
public class Follow {
    @Id
    private String id;
    private String userCurrent;
    private String userFollowing;
    private long dateFollowed;

    public Follow() {
        super();
    }

    public Follow(String id, String userCurrent, String userFollowing, long dateFollowed) {
        this.id = id;
        this.userCurrent = userCurrent;
        this.userFollowing = userFollowing;
        this.dateFollowed = dateFollowed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserCurrent() {
        return userCurrent;
    }

    public void setUserCurrent(String userCurrent) {
        this.userCurrent = userCurrent;
    }

    public String getUserFollowing() {
        return userFollowing;
    }

    public void setUserFollowing(String userFollowing) {
        this.userFollowing = userFollowing;
    }

    public long getDateFollowed() {
        return dateFollowed;
    }

    public void setDateFollowed(long dateFollowed) {
        this.dateFollowed = dateFollowed;
    }
}
