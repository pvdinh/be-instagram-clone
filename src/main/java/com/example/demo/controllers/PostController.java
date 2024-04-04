package com.example.demo.controllers;

import com.example.demo.helper.RedisHelper;
import com.example.demo.models.Post;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.example.demo.services.CommentService;
import com.example.demo.services.PostService;
import com.example.demo.services.SavedPostService;
import com.example.demo.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    private RedisHelper redisHelper;

    @GetMapping
    public BaseResponse findAllPost() {
        try {
            return new ResponseData(HttpStatus.OK.value(), postService.findAll());
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/{userId}")
    public BaseResponse findAllPostByUserId(@PathVariable(name = "userId") String userId) {
        try {
            return new ResponseData(HttpStatus.OK.value(), postService.findAllByUserId(userId));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @PostMapping("/fololwing")
    public BaseResponse findAllPostByListUserId(@RequestBody Map<String, List<String>> listUserId) {
        try {
            return new ResponseData(HttpStatus.OK.value(), postService.findAllByListUserId(listUserId.get("following")));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @PostMapping("/{postId}/like")
    public BaseResponse like(@PathVariable(name = "postId") String postId) {
        try {
            return new ResponseMessage(HttpStatus.OK.value(), postService.like(userAccountService.getUID(), postId));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @PostMapping("/{postId}/unlike")
    public BaseResponse unLike(@PathVariable(name = "postId") String postId) {
        try {
            return new ResponseMessage(HttpStatus.ACCEPTED.value(), postService.unLike(userAccountService.getUID(), postId));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/{postId}/get")
    public BaseResponse getPostInformationOfUser(@PathVariable(name = "postId") String postId) {
        try {
            return new ResponseObject(HttpStatus.OK.value(), postService.getPostInformationOfUser(postId));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/following")
    public BaseResponse getAllPostFollowingUser(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        try {
            return new ResponseObject(HttpStatus.OK.value(), postService.getAllPostInformationFollowing(page, size));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/{postId}/comment")
    public BaseResponse getAllCommentInPost(@PathVariable(name = "postId") String pId, @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseObject(HttpStatus.OK.value(), commentService.findCommentByIdPost(pId));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    //    @PostMapping("/comment")
//    public BaseResponse addCommentInPost(@RequestBody Comment comment){
//        try{
//            return new ResponseMessage(HttpStatus.OK.value(),commentService.addCommentInPost(comment));
//        }catch (Exception e){
//            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
//        }
//    }
    @PostMapping
    public BaseResponse postNewPost(@RequestBody Post post) {
        try {
            return new ResponseMessage(HttpStatus.OK.value(), postService.postNewPost(post));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @PutMapping
    public BaseResponse changePrivacy(@RequestBody Post post) {
        try {
            return new ResponseMessage(HttpStatus.OK.value(), postService.changePrivacy(post));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @DeleteMapping("{postId}/delete")
    public BaseResponse deletePost(@PathVariable(name = "postId") String pId) {
        try {
            return new ResponseMessage(HttpStatus.OK.value(), postService.deletePost(pId));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    //Save post

    @GetMapping("/{postId}/check-save-post")
    public BaseResponse checkSavedPost(@PathVariable(name = "postId") String postId) {
        try {
            return new ResponseMessage(HttpStatus.OK.value(), String.valueOf(savedPostService.checkSavedPost(postId)));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @PostMapping("{postId}/begin-save-post")
    public BaseResponse beginSavePost(@PathVariable(name = "postId") String postId) {
        try {
            return new ResponseMessage(HttpStatus.OK.value(), savedPostService.beginSavePost(postId));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }


    @PostMapping("{postId}/end-save-post")
    public BaseResponse endSavePost(@PathVariable(name = "postId") String postId) {
        try {
            return new ResponseMessage(HttpStatus.OK.value(), savedPostService.endSavePost(postId));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/top-1-like")
    public BaseResponse getTop1Like() {
        try {
            Post post = postService.getTop1Like();
            return new ResponseObject(HttpStatus.OK.value(), postService.getTop1Like());
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/top-1-comment")
    public BaseResponse getTop1Comment() {
        try {
            return new ResponseObject(HttpStatus.OK.value(), postService.getTop1Comment());
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/top-1-save")
    public BaseResponse getTop1Save() {
        try {
            return new ResponseObject(HttpStatus.OK.value(), postService.getTop1Save());
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/top-1-popular")
    public BaseResponse getTop1Popular() {
        try {
            return new ResponseObject(HttpStatus.OK.value(), postService.getTop1Popular());
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

}
