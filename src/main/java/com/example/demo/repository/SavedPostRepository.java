package com.example.demo.repository;

import com.example.demo.models.saved_post.SavedPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SavedPostRepository extends MongoRepository<SavedPost,String> {
    List<SavedPost> findSavedPostByUserId(String userId);
    SavedPost findSavedPostByUserIdAndPostId(String userId,String postId);
    List<SavedPost> findSavedPostByPostId(String postId);
    void deleteByPostId(String idPost);
    void deleteByUserId(String idUser);
}
