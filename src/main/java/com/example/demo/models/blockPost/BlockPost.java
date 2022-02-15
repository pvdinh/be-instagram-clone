package com.example.demo.models.blockPost;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BlockPost {

    @Id
    private String id;
    private String postId;
    private String imagePath;
    private String videoPath;
    private long dateCreated;

    public BlockPost() {
        super();
    }

    public BlockPost(String postId, String imagePath, String videoPath, long dateCreated) {
        this.postId = postId;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }
}
