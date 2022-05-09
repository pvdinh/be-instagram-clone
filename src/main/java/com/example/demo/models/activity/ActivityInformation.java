package com.example.demo.models.activity;

import com.example.demo.models.Post;
import com.example.demo.models.UserAccountSetting;

import java.util.List;

public class ActivityInformation {
    private Activity activity;
    private UserAccountSetting userAccountSetting;
    private Post post;
    private List<String> likes;
    private List<String> comments;
    private List<String> likeComments;
    private List<String> replyComments;

    public ActivityInformation() {
        super();
    }

    public ActivityInformation(Activity activity, UserAccountSetting userAccountSetting, Post post, List<String> likes, List<String> comments) {
        this.activity = activity;
        this.userAccountSetting = userAccountSetting;
        this.post = post;
        this.likes = likes;
        this.comments = comments;
    }

    public ActivityInformation(Activity activity, UserAccountSetting userAccountSetting, Post post, List<String> likes, List<String> comments, List<String> likeComments, List<String> replyComments) {
        this.activity = activity;
        this.userAccountSetting = userAccountSetting;
        this.post = post;
        this.likes = likes;
        this.comments = comments;
        this.likeComments = likeComments;
        this.replyComments = replyComments;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getLikeComments() {
        return likeComments;
    }

    public void setLikeComments(List<String> likeComments) {
        this.likeComments = likeComments;
    }

    public List<String> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<String> replyComments) {
        this.replyComments = replyComments;
    }
}
