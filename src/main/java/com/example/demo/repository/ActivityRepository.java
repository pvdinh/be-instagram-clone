package com.example.demo.repository;

import com.example.demo.models.activity.Activity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends MongoRepository<Activity,String> {
    Activity findActivityById(String id);
    Activity findActivityByIdCurrentUserAndIdInteractUserAndTypeActivity(String idCurrentUser,String idInteractUser,String typeActivity);
    Activity findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(String idCurrentUser,String idInteractUser,String typeActivity,String idPost);
    List<Activity> findByIdInteractUser(String idInteractUser, Pageable pageable);
    void deleteByIdPost(String idPost);
    void deleteByIdCurrentUser(String idCurrentUser);
    void deleteByIdInteractUser(String idInteractUser);
}
