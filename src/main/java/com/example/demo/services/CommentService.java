package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.models.activity.Activity;
import com.example.demo.models.comment.Comment;
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
    public String addCommentInPost(Comment comment,String usernameCurrentUser){
        try {
            Post post = postService.findByPostId(comment.getIdPost());
            if(post != null){
                commentRepository.insert(comment);
                //Thêm vào activity
                UserAccount userAccount = userAccountService.findUserAccountByUsername(usernameCurrentUser);
                if(!userAccount.getId().equals(post.getUserId())){
                    activityService.insert(new Activity(userAccount.getId(),post.getUserId(),post.getId(),"comment",0,System.currentTimeMillis()),userAccount.getId());
                }
                return SUCCESS;
            } return FAIL;
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

    //user delete
    public String delete(Comment comment, String usernameCurrentUser){
        try {
            //muốn xoá được bình luận hoặc là người đăng bài, hoặc là người bình luận
            //tim xem ai la nguoi dang bai
            Post post = postService.findByPostId(comment.getIdPost());
            if(post != null){
                //xac nhan xem binh luan do la cua ai
                UserAccount userAccount = userAccountService.findUserAccountByUsername(usernameCurrentUser);
                if(comment.getIdUser().equals(userAccount.getId()) || userAccount.getId().equals(post.getUserId())){
                    commentRepository.deleteById(comment.getId());

                    //khi mà post không còn bình luận nào thì xoá activity
                    List<Comment> commentList = commentRepository.findCommentByIdUserAndIdPost(comment.getIdUser(),post.getId());
                    if(commentList.size() == 0){
                        Activity activity= activityService.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(comment.getIdUser(),post.getUserId(),"comment",post.getId());
                        activityService.delete(activity,userAccount.getId());
                    }
                    return SUCCESS;
                }else return FAIL;
            }else return FAIL;
        }catch (Exception e){
            return FAIL;
        }
    }

    //admin delete
    public String deleteComment(String id){
        try {
            commentRepository.deleteById(id);
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

}
