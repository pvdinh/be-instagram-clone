package com.example.demo.repository;

import com.example.demo.models.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LikeRepository extends MongoRepository<Like,String> {
    List<Like> findLikeByIdPost(String idPost);
    Like findLikeByIdUserAndIdPost(String idUser,String idPoss);
}
