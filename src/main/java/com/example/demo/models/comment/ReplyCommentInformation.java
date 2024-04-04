package com.example.demo.models.comment;

import com.example.demo.models.UserAccountSetting;

public class ReplyCommentInformation {
    private UserAccountSetting userAccountSetting;
    private ReplyComment replyComment;

    public ReplyCommentInformation() {
        super();
    }

    public ReplyCommentInformation(UserAccountSetting userAccountSetting, ReplyComment replyComment) {
        this.userAccountSetting = userAccountSetting;
        this.replyComment = replyComment;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }

    public ReplyComment getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(ReplyComment replyComment) {
        this.replyComment = replyComment;
    }
}
