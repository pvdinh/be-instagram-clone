package com.example.demo.utils;

import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.PostInformation;

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
}
