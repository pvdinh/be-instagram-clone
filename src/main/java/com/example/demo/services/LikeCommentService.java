package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.models.activity.Activity;
import com.example.demo.models.comment.Comment;
import com.example.demo.models.comment.LikeComment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeCommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LikeCommentService {
    @Autowired
    private LikeCommentRepository likeCommentRepository;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<String> findByIdComment(String idComment) {
        List<String> idUsers = new ArrayList<>();
        likeCommentRepository.findByIdComment(idComment).forEach(likeComment -> {
            idUsers.add(likeComment.getIdUser());
        });
        return idUsers;
    }

    public String like(String idUser, String idComment) {
        LikeComment likeComment = new LikeComment(idComment, idUser, System.currentTimeMillis());
        LikeComment likeCommentD = likeCommentRepository.findByIdCommentAndIdUser(idComment, idUser);
        if (likeCommentD == null) {
            likeCommentRepository.insert(likeComment);
            //them vao activity
            Optional<Comment> commentO = commentRepository.findById(idComment);
            Comment comment = commentO.orElse(new Comment());
            Post post = postRepository.findPostById(comment.getIdPost());
            if(!idUser.equals(post.getUserId()) && !idUser.equals(comment.getIdUser())){
                activityService.insert(new Activity(userAccountService.getUID(),comment.getIdUser(),post.getId(),"likeComment",0,System.currentTimeMillis()),userAccountService.getUID());
            }
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public String unLike(String idUser, String idComment) {
        LikeComment likeComment = likeCommentRepository.findByIdCommentAndIdUser(idComment, idUser);
        if (likeComment != null) {
            likeCommentRepository.delete(likeComment);
            //xoa khoi activity
            Optional<Comment> commentO = commentRepository.findById(idComment);
            Comment comment = commentO.orElse(new Comment());
            Post post = postRepository.findPostById(comment.getIdPost());
            if(!idUser.equals(post.getUserId()) && !idUser.equals(comment.getIdUser())){
                Activity activity = activityService.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(userAccountService.getUID(), comment.getIdUser(), "likeComment", userAccountService.getUID());
                activityService.delete(activity,comment.getIdUser());
            }
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public List<UserAccountSetting> findUserAccountSettingLikedCommentPost(String idComment,int page,int size){
        List<UserAccountSetting> userAccountSettings = new ArrayList<>();
        Pageable pageable = PageRequest.of(page,size, Sort.by("dateCreated").descending());
        List<LikeComment> likeComments = likeCommentRepository.findByIdComment(idComment,pageable);
        likeComments.forEach(likeComment -> {
            userAccountSettings.add(userAccountSettingRepository.findUserAccountSettingById(likeComment.getIdUser()));
        });
        return userAccountSettings;
    }

}
