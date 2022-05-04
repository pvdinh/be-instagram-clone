package com.example.demo.repository;

import com.example.demo.models.comment.ReplyComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyCommentRepository extends MongoRepository<ReplyComment,String> {
    List<ReplyComment> findByIdComment(String idComment);
    List<ReplyComment> findReplyCommentByIdUserAndIdPostAndIdComment(String idUser,String idPost,String idComment);
    void deleteAllByIdPost(String idPost);
}
