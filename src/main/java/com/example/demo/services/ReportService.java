package com.example.demo.services;

import com.example.demo.models.report.Report;
import com.example.demo.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserAccountService userAccountService;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public List<Report> findAllPageable(int page, int size) {
        List<Report> reports = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
            reports = reportRepository.findAll(pageable).getContent();
            return reports;
        } catch (Exception e) {
            return reports;
        }
    }

    public String insert(Report report) {
        try {
            report.setIdUser(userAccountService.getUID());
            reportRepository.insert(report);
            return SUCCESS;
        } catch (Exception e) {
            return FAIL;
        }
    }

    public List<Report> findContainsByIdUserOrIdPost(String s) {
        List<Report> reports = new ArrayList<>();
        try {
            reports = reportRepository.findByIdUserContainsOrIdPostContains(s, s);
            return reports;
        } catch (Exception e) {
            return reports;
        }
    }

    public List<Report> findContainsByIdUserOrIdPostPageable(String s, int page, int size) {
        List<Report> reports = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
            reports = reportRepository.findByIdUserContainsOrIdPostContains(s, s, pageable);
            return reports;
        } catch (Exception e) {
            return reports;
        }
    }
}
