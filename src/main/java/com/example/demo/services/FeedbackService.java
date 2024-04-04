package com.example.demo.services;

import com.example.demo.models.feedback.Feedback;
import com.example.demo.models.report.Report;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<Feedback> findAll(){
        return feedbackRepository.findAll();
    }

    public List<Feedback> findContainsByIdUserOrSubjectOrContentPageable(String s,int page,int size){
        List<Feedback> feedbacks = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page,size,Sort.by("dateCreated").descending());
            feedbacks = feedbackRepository.findByIdUserContainsOrSubjectContainsOrContentContains(s,s,s,pageable);
            return feedbacks;
        }catch (Exception e){
            return feedbacks;
        }
    }

    public List<Feedback> findContainsByIdUserOrSubjectOrContent(String s){
        List<Feedback> feedbacks = new ArrayList<>();
        try {
            feedbacks = feedbackRepository.findByIdUserContainsOrSubjectContainsOrContentContains(s,s,s);
            return feedbacks;
        }catch (Exception e){
            return feedbacks;
        }
    }

    public List<Feedback> findAllPageable(int page,int size){
        List<Feedback> feedbacks = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page,size, Sort.by("dateCreated").descending());
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

    public String delete(String id){
        try {
            feedbackRepository.deleteById(id);
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public String deleteByIdUser(String idUser){
        try {
            feedbackRepository.deleteByIdUser(idUser);
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public List<Feedback> filterByTime(long start,long end){
        List<Feedback> feedbacks = new ArrayList<>();
        try {
            feedbacks = feedbackRepository.filterByTime(start,end);
            return feedbacks;
        }catch (Exception e){
            return feedbacks;
        }
    }


    public List<Feedback> filterByTimePageable(long start,long end,int page,int size){
        List<Feedback> feedbacks = new ArrayList<>();
        try {
            Pageable pageable=PageRequest.of(page,size, Sort.by("dateCreated").descending());
            feedbacks = feedbackRepository.filterByTime(start,end, pageable);
            return feedbacks;
        }catch (Exception e){
            return feedbacks;
        }
    }
}
