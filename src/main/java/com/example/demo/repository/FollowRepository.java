package com.example.demo.repository;

import com.example.demo.models.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FollowRepository extends MongoRepository<Follow,String> {
    List<Follow> findFollowByUserCurrent(String uCId);
    List<Follow> findFollowByUserCurrent(String uCId, Pageable pageable);
    List<Follow> findFollowByUserFollowing(String uFId);
    List<Follow> findFollowByUserFollowing(String uFId, Pageable pageable);
    Follow findFollowsByUserCurrentAndUserFollowing(String uCId,String uFId);

    void deleteByUserCurrent(String id);
    void deleteByUserFollowing(String id);
}
