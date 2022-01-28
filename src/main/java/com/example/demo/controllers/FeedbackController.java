package com.example.demo.controllers;

import com.example.demo.models.feedback.Feedback;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public BaseResponse insertFeedback(@RequestBody Feedback feedback){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),feedbackService.insert(feedback));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }
}
