package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.models.activity.Activity;
import com.example.demo.models.comment.Comment;
import com.example.demo.models.comment.LikeComment;
import com.example.demo.models.comment.ReplyComment;
import com.example.demo.models.comment.ReplyCommentInformation;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReplyCommentService {
    @Autowired
    private ReplyCommentRepository replyCommentRepository;
    @Autowired
    private LikeCommentRepository likeCommentRepository;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public String replyComment(ReplyComment replyComment){
        try {
            replyCommentRepository.insert(replyComment);
            //them vao activity
            Post post = postRepository.findPostById(replyComment.getIdPost());
            UserAccount userAccount = userAccountService.findUserAccountById(replyComment.getIdUser());
            if(!post.getUserId().equals(userAccount.getId())){
                //activity đối với chủ bài đăng
                Activity a1 = new Activity(userAccount.getId(),post.getUserId(),post.getId(),"comment",0,System.currentTimeMillis());
                activityService.insert(a1,userAccount.getId());
                //activity đối với người comment trong bài
                Optional<Comment> comment = commentRepository.findById(replyComment.getIdComment());
                Comment cmt = comment.orElse(new Comment());
                Activity a2 = new Activity(userAccount.getId(),cmt.getIdUser(),post.getId(),"replyComment",0,System.currentTimeMillis());
                activityService.insert(a2,userAccount.getId());
            }
            return  SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public String deleteReplyComment(ReplyComment replyComment){
        try {
            //muốn xoá được bình luận hoặc là người đăng bài, hoặc là người bình luận
            //tim xem ai la nguoi dang bai
            Post post = postService.findByPostId(replyComment.getIdPost());
            UserAccount userAccount = userAccountService.findUserAccountById(replyComment.getIdUser());
            if(replyComment.getIdUser().equals(userAccount.getId()) || userAccount.getId().equals(post.getUserId())){
                replyCommentRepository.deleteById(replyComment.getId());

                //khi mà post không còn bình luận nào thì xoá activity
                List<ReplyComment> commentList = replyCommentRepository.findReplyCommentByIdUserAndIdPostAndIdComment(replyComment.getIdUser(),post.getId(),replyComment.getIdComment());
                if(commentList.size() == 0){
                    Activity activity= activityService.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(replyComment.getIdUser(),post.getUserId(),"replyComment",post.getId());
                    activityService.delete(activity,userAccount.getId());
                }
            } else return FAIL;
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public List<ReplyCommentInformation> findByIdComment(String idComment,int page,int size){
        List<ReplyCommentInformation> replyCommentInformations = new ArrayList<>();
        List<ReplyComment> replyComments = replyCommentRepository.findByIdComment(idComment);
        replyComments.forEach(replyComment -> {
            UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(replyComment.getIdUser());
            replyCommentInformations.add(new ReplyCommentInformation(userAccountSetting,replyComment));
        });
        try {
            return replyCommentInformations;
        }catch (Exception e){
            return replyCommentInformations;
        }
    }

    public String like(String idUser, String idReplyComment) {
        LikeComment likeComment = new LikeComment(idReplyComment, idUser, System.currentTimeMillis());
        LikeComment likeCommentD = likeCommentRepository.findByIdCommentAndIdUser(idReplyComment, idUser);
        if (likeCommentD == null) {
            likeCommentRepository.insert(likeComment);
            //them vao activity
            Optional<ReplyComment> commentO = replyCommentRepository.findById(idReplyComment);
            ReplyComment comment = commentO.orElse(new ReplyComment());
            Post post = postRepository.findPostById(comment.getIdPost());
            if(!idUser.equals(post.getUserId())){
                activityService.insert(new Activity(userAccountService.getUID(),comment.getIdUser(),post.getId(),"likeReplyComment",0,System.currentTimeMillis()),userAccountService.getUID());
            }
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public String unLike(String idUser, String idReplyComment) {
        LikeComment likeComment = likeCommentRepository.findByIdCommentAndIdUser(idReplyComment, idUser);
        if (likeComment != null) {
            likeCommentRepository.delete(likeComment);
            //xoa khoi activity
            Optional<ReplyComment> commentO = replyCommentRepository.findById(idReplyComment);
            ReplyComment comment = commentO.orElse(new ReplyComment());
            Post post = postRepository.findPostById(comment.getIdPost());
            if(!idUser.equals(post.getUserId())){
                Activity activity = activityService.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(userAccountService.getUID(), comment.getIdUser(), "likeReplyComment", post.getId());
                activityService.delete(activity,userAccountService.getUID());
            }
            return SUCCESS;
        } else {
            return FAIL;
        }
    }
}
