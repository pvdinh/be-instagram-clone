package com.example.demo.models;

import com.example.demo.models.comment.Comment;

public class CommentInformation {
    private UserAccountSetting userAccountSetting;
    private Comment comment;

    public CommentInformation() {
        super();
    }

    public CommentInformation(UserAccountSetting userAccountSetting, Comment comment) {
        this.userAccountSetting = userAccountSetting;
        this.comment = comment;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
