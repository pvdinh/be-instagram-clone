package com.example.demo.repository;

import com.example.demo.models.blockPost.BlockPost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlockPostRepository extends MongoRepository<BlockPost,String> {
    BlockPost findByPostId(String pId);
}
