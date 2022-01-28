package com.example.demo.repository;

import com.example.demo.models.report.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<Report,String> {
    List<Report> findByIdUserContainsOrIdPostContains(String idUser,String idPost);
    List<Report> findByIdUserContainsOrIdPostContains(String idUser, String idPost, Pageable pageable);
}
