package com.example.demo.repository;

import com.example.demo.models.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post,String> {
    Post findPostById(String id);
    List<Post> findPostByUserId(String userId);
    List<Post> findPostByUserId(String userId,Pageable pageable);
    List<Post> findPostByIdGroup(String idGroup);
    List<Post> findPostByIdGroup(String idGroup,Pageable pageable);

    List<Post> findPostVideoByTypeAndUserId(String type, String uId, Pageable pageable);
    List<Post> findPostByPrivacyAndUserId(Integer privacy,String idUser, Pageable pageable);

    List<Post> findByUserIdContainsOrId(String uId,String pId,Pageable pageable);
    List<Post> findByUserIdContainsOrId(String uId,String pId);
}
