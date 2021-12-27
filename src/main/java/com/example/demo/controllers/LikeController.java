package com.example.demo.controllers;

import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @GetMapping("/{postId}")
    public BaseResponse findAllUserAccountSettingByPostId(@PathVariable(name = "postId") String postId, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        try{
            return new ResponseData(HttpStatus.OK.value(),likeService.findAllUserAccountSettingByPostId(postId,page,size));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }
}
