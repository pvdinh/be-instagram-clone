package com.example.demo.repository;

import com.example.demo.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findCommentByIdPost(String pId);
}
