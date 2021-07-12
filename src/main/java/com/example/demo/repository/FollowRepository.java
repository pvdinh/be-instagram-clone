package com.example.demo.repository;

import com.example.demo.models.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FollowRepository extends MongoRepository<Follow,String> {
    List<Follow> findFollowByUserCurrent(String uCId);
    List<Follow> findFollowByUserFollowing(String uFId);
    Follow findFollowsByUserCurrentAndUserFollowing(String uCId,String uFId);
}
