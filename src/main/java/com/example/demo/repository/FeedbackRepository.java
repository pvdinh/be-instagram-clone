package com.example.demo.repository;

import com.example.demo.models.feedback.Feedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FeedbackRepository extends MongoRepository<Feedback,String> {
    List<Feedback> findByIdUserContainsOrSubjectContainsOrContentContains(String idUser,String subject,String content, Pageable pageable);
    List<Feedback> findByIdUserContainsOrSubjectContainsOrContentContains(String idUser,String subject,String content);

    void deleteByIdUser(String idUser);

    @Query("{'dateCreated': {$gte: ?0, $lte:?1 }}")
    List<Feedback> filterByTime(Long start,Long end);
    @Query("{'dateCreated': {$gte: ?0, $lte:?1 }}")
    List<Feedback> filterByTime(Long start,Long end,Pageable pageable);
}
