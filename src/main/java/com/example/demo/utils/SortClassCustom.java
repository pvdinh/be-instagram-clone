package com.example.demo.utils;

import com.example.demo.models.Comment;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.PostInformation;
import com.example.demo.models.profile.PostDetail;

import java.util.Comparator;

public class SortClassCustom {
    public static class PostByDateCreate implements Comparator<PostInformation> {
        @Override
        public int compare(PostInformation o1, PostInformation o2) {
            return Long.compare(o2.getPost().getDateCreated(), o1.getPost().getDateCreated());
        }
    }

    public static class LikeByDateLiked implements Comparator<Like> {   
        @Override
        public int compare(Like o1, Like o2) {
            return Long.compare(o2.getDateLiked(), o1.getDateLiked());
        }
    }

    public static class LikeByDateCommented implements Comparator<Comment> {
        @Override
        public int compare(Comment o1, Comment o2) {
            return Long.compare(o2.getDateCommented(),o1.getDateCommented());
        }
    }

    public static class PostProfileByDateCreate implements Comparator<PostDetail>{
        @Override
        public int compare(PostDetail o1, PostDetail o2) {
            return Long.compare(o2.getPost().getDateCreated(), o1.getPost().getDateCreated());
        }
    }
}
