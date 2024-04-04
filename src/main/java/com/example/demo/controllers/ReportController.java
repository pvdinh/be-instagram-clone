package com.example.demo.controllers;

import com.example.demo.models.report.Report;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping
    public BaseResponse insertReport(@RequestBody Report report){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),reportService.insert(report));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }
}
