package com.example.demo.controllers;


import com.example.demo.models.comment.ReplyComment;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.LikeCommentService;
import com.example.demo.services.ReplyCommentService;
import com.example.demo.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private LikeCommentService likeCommentService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ReplyCommentService replyCommentService;


    @GetMapping("/{idComment}")
    public BaseResponse findByIdComment(@PathVariable(name = "idComment") String idComment) {
        try{
            return new ResponseData(HttpStatus.OK.value(), likeCommentService.findByIdComment(idComment));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @PostMapping("/{idComment}/like")
    public BaseResponse like(@PathVariable(name = "idComment") String idComment){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),likeCommentService.like(userAccountService.getUID(),idComment));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }
    @PostMapping("/{idComment}/unlike")
    public BaseResponse unLike(@PathVariable(name = "idComment") String idComment){
        try{
            return new ResponseMessage(HttpStatus.ACCEPTED.value(),likeCommentService.unLike(userAccountService.getUID(),idComment));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @GetMapping("/{idComment}/all")
    public BaseResponse findUserAccountSettingLikedCommentPost(@PathVariable(name = "idComment") String postId, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        try{
            return new ResponseData(HttpStatus.OK.value(),likeCommentService.findUserAccountSettingLikedCommentPost(postId,page,size));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @GetMapping("/{idComment}/all-reply")
    public BaseResponse findAllReplyComment(@PathVariable(name = "idComment")String idComment, @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size)
    {
        try{
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseData(HttpStatus.OK.value(),replyCommentService.findByIdComment(idComment,currentPage,pageSize));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @PostMapping("/{idComment}/like-reply-comment")
    public BaseResponse likeReplyComment(@PathVariable(name = "idComment") String idComment){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),replyCommentService.like(userAccountService.getUID(),idComment));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }
    @PostMapping("/{idComment}/unlike-reply-comment")
    public BaseResponse unLikeReplyComment(@PathVariable(name = "idComment") String idComment){
        try{
            return new ResponseMessage(HttpStatus.ACCEPTED.value(),replyCommentService.unLike(userAccountService.getUID(),idComment));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }
}
