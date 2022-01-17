package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<ReportRepository,String> {
}
