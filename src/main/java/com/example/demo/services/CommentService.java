package com.example.demo.services;

import com.example.demo.models.Comment;
import com.example.demo.models.CommentInformation;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;


    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<CommentInformation> findCommentByIdPost(String pId){
        List<CommentInformation> commentInformations = new ArrayList<>();
        List<Comment> comments = commentRepository.findCommentByIdPost(pId);
        comments.forEach(comment -> {
            commentInformations.add(new CommentInformation(userAccountSettingRepository.findUserAccountSettingById(comment.getIdUser()),comment));
        });
        return commentInformations;
    }
    public String addCommentInPost(Comment comment){
        commentRepository.insert(comment);
        return SUCCESS;
    }
}
