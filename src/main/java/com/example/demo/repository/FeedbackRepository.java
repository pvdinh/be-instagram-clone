package com.example.demo.repository;

import com.example.demo.models.feedback.Feedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FeedbackRepository extends MongoRepository<Feedback,String> {
    List<Feedback> findByIdUserContainsOrSubjectContainsOrContentContains(String idUser,String subject,String content, Pageable pageable);
    List<Feedback> findByIdUserContainsOrSubjectContainsOrContentContains(String idUser,String subject,String content);

    @Query("{'dateCreated': {$gte: ?0, $lte:?1 }}")
    List<Feedback> filterByTime(Long start,Long end);
    @Query("{'dateCreated': {$gte: ?0, $lte:?1 }}")
    List<Feedback> filterByTime(Long start,Long end,Pageable pageable);
}
