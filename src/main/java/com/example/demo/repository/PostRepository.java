package com.example.demo.repository;

import com.example.demo.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post,String> {
    Post findPostById(String id);
    List<Post> findPostByUserId(String userId);
}
