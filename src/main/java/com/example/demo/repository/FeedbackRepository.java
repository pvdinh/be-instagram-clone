package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepository extends MongoRepository<FeedbackRepository,String> {
}
