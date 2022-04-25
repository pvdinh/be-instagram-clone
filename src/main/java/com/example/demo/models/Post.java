package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document
public class Post {
    @Id
    private String id;
    private String caption;
    private String imagePath;
    private String tags;
    private String userId;
    private long dateCreated;
    private List<String> likes;
    private String type;
    private String videoPath;
    private int isBlock;

    public Post() {
        super();
    }

    public Post(String id, String caption, String imagePath, String tags, String userId, long dateCreated, List<String> likes, String type, String videoPath) {
        this.id = id;
        this.caption = caption;
        this.imagePath = imagePath;
        this.tags = tags;
        this.userId = userId;
        this.dateCreated = dateCreated;
        this.likes = likes;
        this.type = type;
        this.videoPath = videoPath;
        this.isBlock = 0;
    }

    public Post(String id, String caption, String imagePath, String tags, String userId, long dateCreated, List<String> likes, String type, String videoPath, int isBlock) {
        this.id = id;
        this.caption = caption;
        this.imagePath = imagePath;
        this.tags = tags;
        this.userId = userId;
        this.dateCreated = dateCreated;
        this.likes = likes;
        this.type = type;
        this.videoPath = videoPath;
        this.isBlock = isBlock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(int isBlock) {
        this.isBlock = isBlock;
    }
}
