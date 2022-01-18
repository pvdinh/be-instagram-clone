package com.example.demo.controllers;

import com.example.demo.models.feedback.Feedback;
import com.example.demo.models.report.Report;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public BaseResponse findAllPageable(@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size){
        try{
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            List<Feedback> feedbacks = feedbackService.findAllPageable(currentPage,pageSize);
            return new ResponseData(HttpStatus.OK.value(),feedbacks,feedbacks.size());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @PostMapping
    public BaseResponse insertFeedback(@RequestBody Feedback feedback){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),feedbackService.insert(feedback));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }
}
