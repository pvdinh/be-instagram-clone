package com.example.demo.repository;

import com.example.demo.models.saved_post.SavedPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SavedPostRepository extends MongoRepository<SavedPost,String> {
    List<SavedPost> findSavedPostByUserId(String userId);
    SavedPost findSavedPostByUserIdAndPostId(String userId,String postId);
    List<SavedPost> findSavedPostByPostId(String postId);
    void deleteByPostId(String idPost);
    void deleteByUserId(String idUser);
}
