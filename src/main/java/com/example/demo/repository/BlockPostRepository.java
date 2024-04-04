package com.example.demo.repository;

import com.example.demo.models.blockPost.BlockPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockPostRepository extends MongoRepository<BlockPost,String> {
    BlockPost findByPostId(String pId);
}
