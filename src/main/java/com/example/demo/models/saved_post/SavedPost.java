package com.example.demo.models.saved_post;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndex(name = "userId_postId",def = "{'userId': 1 , 'postId' : 1}", unique = true)
public class SavedPost {
    @Id
    private String id;
    private String userId;
    private String postId;
    private long dateSaved;

    public SavedPost() {
        super();
    }

    public SavedPost(String userId, String postId, long dateSaved) {
        this.userId = userId;
        this.postId = postId;
        this.dateSaved = dateSaved;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getDateSaved() {
        return dateSaved;
    }

    public void setDateSaved(long dateSaved) {
        this.dateSaved = dateSaved;
    }
}
