package com.example.demo.services;

import com.example.demo.models.feedback.Feedback;
import com.example.demo.models.report.Report;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private UserAccountService userAccountService;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<Feedback> findAllPageable(int page,int size){
        List<Feedback> feedbacks = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page,size);
            feedbacks = feedbackRepository.findAll(pageable).getContent();
            return feedbacks;
        }catch (Exception e){
            return feedbacks;
        }
    }

    public String insert(Feedback feedback){
        try{
            feedback.setIdUser(userAccountService.getUID());
            feedbackRepository.insert(feedback);
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }
}
