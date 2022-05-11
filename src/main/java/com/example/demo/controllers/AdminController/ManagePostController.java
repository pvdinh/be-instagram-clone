package com.example.demo.controllers.AdminController;

import com.example.demo.models.Post;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.example.demo.services.CommentService;
import com.example.demo.services.LikeService;
import com.example.demo.services.PostService;
import com.example.demo.services.SavedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/manage-post")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ManagePostController {
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SavedPostService savedPostService;
    @Autowired
    private LikeService likeService;

    //http://localhost:8080/api/v1/admin/manage-post/{postId}/get
    @GetMapping("/{postId}/get")
    public BaseResponse getPostInformationOfUser(@PathVariable(name = "postId") String postId){
        try{
            return new ResponseObject(HttpStatus.OK.value(),postService.adminGetPostInformation(postId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-post/{postId}/comment
    @GetMapping("/{postId}/comment")
    public BaseResponse getAllCommentInPost(@PathVariable(name = "postId")String pId){
        try{
            return new ResponseObject(HttpStatus.OK.value(),commentService.findCommentByIdPost(pId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-post/{postId}/get-user-saved
    @GetMapping("/{postId}/get-user-saved")
    public BaseResponse getAllUserSavedPost(@PathVariable(name = "postId") String postId){
        try{
            return new ResponseObject(HttpStatus.OK.value(),savedPostService.getAllUserSavedPost(postId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-post/{postId}/get-user-liked
    @GetMapping("/{postId}/get-user-liked")
    public BaseResponse getAllUserLikedPost(@PathVariable(name = "postId") String postId){
        try{
            return new ResponseData(HttpStatus.OK.value(),likeService.getAllUserLikedPost(postId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-post/get-all-post
    // filer = 1 : oldest
    // filer = 2 : latest
    // filer = 3 : popular
    @GetMapping("/get-all-post")
    public BaseResponse getAllPost(@RequestParam(name = "filter") Optional<Integer> filter,@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size){
        try{
            int typeFilter = filter.orElse(1);
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseData(HttpStatus.OK.value(),postService.findPostFilter(typeFilter,currentPage,pageSize),postService.findAll().size());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-post/{search}/search
    @GetMapping("/{search}/search")
    public BaseResponse findContainsByUserIdOrIdPageable(@PathVariable(name = "search") String search, @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size){
        try{
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            List<Post> posts = postService.findContainsByUserIdOrIdPageable(search,currentPage,pageSize);
            return new ResponseData(HttpStatus.OK.value(),posts,postService.findContainsByUserIdOrId(search).size());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-post/{postId}/block
    @PostMapping("{postId}/block")
    public BaseResponse blockPost(@PathVariable(name = "postId") String postId){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),postService.blockPost(postId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-post/{postId}/unblock
    @PostMapping("{postId}/unblock")
    public BaseResponse unBlockPost(@PathVariable(name = "postId") String postId){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),postService.unBlockPost(postId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-post/block
    @GetMapping("/block")
    public BaseResponse getPostBlock(@RequestParam(name = "id") String id){
        try {
            return new ResponseObject(HttpStatus.OK.value(),postService.getPostBlock(id));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-post/delete-comment
    @DeleteMapping("/{id}/delete-comment")
    public BaseResponse deleteComment(@PathVariable(name = "id") String id){
        try {
            return new ResponseObject(HttpStatus.OK.value(),commentService.deleteComment(id));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-post/delete
    @DeleteMapping("/{id}/delete")
    public BaseResponse deletePost(@PathVariable(name = "id") String id){
        try {
            return new ResponseObject(HttpStatus.OK.value(),postService.deletePost(id));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }


}
