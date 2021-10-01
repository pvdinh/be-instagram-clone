package com.example.demo.models.profile;

import com.example.demo.models.Post;
import com.example.demo.models.UserAccountSetting;

import java.util.List;

public class PostDetail {
    private Post post;
    private int numberOfComments;
    private List<String> likes;
    private Long dateBeginStory;

    public PostDetail() {
        super();
    }

    public PostDetail(Post post, int numberOfComments, List<String> likes, Long dateBeginStory) {
        this.post = post;
        this.numberOfComments = numberOfComments;
        this.likes = likes;
        this.dateBeginStory = dateBeginStory;
    }

    public PostDetail(Post post, int numberOfComments, List<String> likes) {
        this.post = post;
        this.numberOfComments = numberOfComments;
        this.likes = likes;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public Long getDateBeginStory() {
        return dateBeginStory;
    }

    public void setDateBeginStory(Long dateBeginStory) {
        this.dateBeginStory = dateBeginStory;
    }
}
