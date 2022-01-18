package com.example.demo.repository;

import com.example.demo.models.feedback.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepository extends MongoRepository<Feedback,String> {
}
