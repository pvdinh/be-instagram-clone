package com.example.demo.repository;

import com.example.demo.models.Like;
import com.example.demo.models.comment.LikeComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LikeCommentRepository extends MongoRepository<LikeComment, String> {
    List<LikeComment> findByIdComment(String idComment);
    List<LikeComment> findByIdComment(String idComment, Pageable pageable);
    void deleteByIdComment(String idComment);
    void deleteByIdUser(String idUser);
    LikeComment findByIdCommentAndIdUser(String idComment,String idUser);
}
