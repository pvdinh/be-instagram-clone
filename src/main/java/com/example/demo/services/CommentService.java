package com.example.demo.services;

import com.example.demo.models.Comment;
import com.example.demo.models.CommentInformation;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.activity.Activity;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import com.example.demo.utils.SortClassCustom;
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
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private PostService postService;


    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<CommentInformation> findCommentByIdPost(String pId){
        List<CommentInformation> commentInformations = new ArrayList<>();
        try {
            List<Comment> comments = commentRepository.findCommentByIdPost(pId);
            comments.forEach(comment -> {
                commentInformations.add(new CommentInformation(userAccountSettingRepository.findUserAccountSettingById(comment.getIdUser()),comment));
            });
            return commentInformations;
        }catch (Exception e){
            return commentInformations;
        }
    }
    public String addCommentInPost(Comment comment){
        try {
            commentRepository.insert(comment);
            //Thêm vào activity
            Post post = postService.findByPostId(comment.getIdPost());
            if(!userAccountService.getUID().equals(post.getUserId())){
                activityService.insert(new Activity(userAccountService.getUID(),post.getUserId(),post.getId(),"comment",0,System.currentTimeMillis()));
            }
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public List<String> getListUserCommentedPost(String pId){
        List<String> listDisplayNameComments = new ArrayList<>();
        List<Comment> comments = commentRepository.findCommentByIdPost(pId);
        comments.sort(new SortClassCustom.LikeByDateCommented());
        comments.forEach(like -> {
            String username = userAccountSettingRepository.findUserAccountSettingById(like.getIdUser()).getUsername();
            if(!listDisplayNameComments.contains(username)){
                listDisplayNameComments.add(username);
            }
        });
        return listDisplayNameComments;
    }
}
