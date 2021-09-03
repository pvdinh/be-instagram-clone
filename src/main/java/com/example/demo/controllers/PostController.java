package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.example.demo.services.CommentService;
import com.example.demo.services.PostService;
import com.example.demo.services.SavedPostService;
import com.example.demo.services.UserAccountService;
import com.example.demo.utils.UsernameFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SavedPostService savedPostService;

    @GetMapping
    public BaseResponse findAllPost() {
        return new ResponseData(HttpStatus.OK.value(), postService.findAll());
    }

    @GetMapping("/{userId}")
    public BaseResponse findAllPostByUserId(@PathVariable(name = "userId") String userId) {
        return new ResponseData(HttpStatus.OK.value(), postService.findAllByUserId(userId));
    }

    @PostMapping("/following")
    public BaseResponse findAllPostByListUserId(@RequestBody Map<String,List<String>> listUserId) {
        return new ResponseData(HttpStatus.OK.value(), postService.findAllByListUserId(listUserId.get("following")));
    }

    @PostMapping("/{postId}/like")
    public BaseResponse like(@PathVariable(name = "postId") String postId){
        return new ResponseMessage(HttpStatus.OK.value(),postService.like(userAccountService.getUID(),postId));
    }
    @PostMapping("/{postId}/unlike")
    public BaseResponse unLike(@PathVariable(name = "postId") String postId){
        return new ResponseMessage(HttpStatus.ACCEPTED.value(),postService.unLike(userAccountService.getUID(),postId));
    }
    @GetMapping("/{postId}/get")
    public BaseResponse getPostInformationOfUser(@PathVariable(name = "postId") String postId){
        return new ResponseObject(HttpStatus.OK.value(),postService.getPostInformationOfUser(postId));
    }
    @GetMapping("/following")
    public BaseResponse getAllPostFollowingUser(){
        return new ResponseObject(HttpStatus.OK.value(),postService.getAllPostInformationFollowing());
    }
    @GetMapping("/{postId}/comment")
    public BaseResponse getAllCommentInPost(@PathVariable(name = "postId")String pId){
        return new ResponseObject(HttpStatus.OK.value(),commentService.findCommentByIdPost(pId));
    }
    @PostMapping("/comment")
    public BaseResponse addCommentInPost(@RequestBody Comment comment){
        return new ResponseMessage(HttpStatus.OK.value(),commentService.addCommentInPost(comment));
    }
    @PostMapping
    public BaseResponse postNewPost(@RequestBody Post post){
        return new ResponseMessage(HttpStatus.OK.value(),postService.postNewPost(post));
    }
    @DeleteMapping("{postId}/delete")
    public BaseResponse postNewPost(@PathVariable(name = "postId")String pId){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),postService.deletePost(pId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }
    @PostMapping("{postId}/begin-save-post")
    public BaseResponse beginSavePost(@PathVariable(name = "postId")String postId){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),savedPostService.beginSavePost(postId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }


    @PostMapping("{postId}/end-save-post")
    public BaseResponse endSavePost(@PathVariable(name = "postId")String postId){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),savedPostService.endSavePost(postId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }
}
