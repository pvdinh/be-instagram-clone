package com.example.demo.models;

import com.example.demo.models.group.Group;

import java.util.List;

public class PostInformation {
    private Post post;
    private UserAccountSetting userAccountSetting;
    private List<String> likes;
    private Group group;

    public PostInformation() {
        super();
    }

    public PostInformation(Post post, UserAccountSetting userAccountSetting, List<String> likes) {
        this.post = post;
        this.userAccountSetting = userAccountSetting;
        this.likes = likes;
    }

    public PostInformation(Post post, UserAccountSetting userAccountSetting, List<String> likes, Group group) {
        this.post = post;
        this.userAccountSetting = userAccountSetting;
        this.likes = likes;
        this.group = group;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
