package com.example.demo.controllers;

import com.example.demo.models.message.Message;
import com.example.demo.models.report.Report;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping
    public BaseResponse findAllPageable(@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size){
        try{
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            List<Report> reports = reportService.findAllPageable(currentPage,pageSize);
            return new ResponseData(HttpStatus.OK.value(),reports,reports.size());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @PostMapping
    public BaseResponse insertReport(@RequestBody Report report){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),reportService.insert(report));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }
}
