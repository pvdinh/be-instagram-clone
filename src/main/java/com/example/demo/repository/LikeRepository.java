package com.example.demo.repository;

import com.example.demo.models.Like;
import com.example.demo.models.activity.Activity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LikeRepository extends MongoRepository<Like,String> {
    List<Like> findLikeByIdPost(String idPost);
    Like findLikeByIdUserAndIdPost(String idUser,String idPost);
    List<Like> findLikeByIdPost(String idPost,Pageable pageable);
    void deleteByIdPost(String idPost);
    void deleteByIdUser(String idUser);
    List<Like> findByIdUser(String idUser);
}
