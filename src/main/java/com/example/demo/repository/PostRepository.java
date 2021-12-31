package com.example.demo.repository;

import com.example.demo.models.Post;
import javafx.geometry.Pos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends MongoRepository<Post,String> {
    Post findPostById(String id);
    List<Post> findPostByUserId(String userId);

    List<Post> findPostVideoByTypeAndUserId(String type, String uId, Pageable pageable);
}
