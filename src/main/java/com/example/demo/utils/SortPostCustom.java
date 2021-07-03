package com.example.demo.utils;

import com.example.demo.models.Post;
import com.example.demo.models.PostInformation;

import java.util.Comparator;

public class SortPostCustom {
    public static class ByDateCreate implements Comparator<PostInformation> {
        @Override
        public int compare(PostInformation o1, PostInformation o2) {
            return Long.compare(o2.getPost().getDateCreated(),o1.getPost().getDateCreated());
        }
    }
}
