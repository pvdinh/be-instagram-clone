package com.example.demo.repository;

import com.example.demo.models.report.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report,String> {
}
