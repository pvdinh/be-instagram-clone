package com.example.demo.repository;

import com.example.demo.models.comment.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findCommentByIdPost(String pId);
    List<Comment> findCommentByIdUserAndIdPost(String uId,String pId);
    void deleteByIdPost(String idPost);
}
