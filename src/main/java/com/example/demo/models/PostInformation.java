package com.example.demo.models;

public class PostInformation {
    private Post post;
    private UserAccountSetting userAccountSetting;

    public PostInformation() {
        super();
    }

    public PostInformation(Post post, UserAccountSetting userAccountSetting) {
        this.post = post;
        this.userAccountSetting = userAccountSetting;
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
}
