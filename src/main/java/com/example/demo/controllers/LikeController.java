package com.example.demo.controllers;

import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @GetMapping("/{postId}")
    public BaseResponse getListUserLikedPost(@PathVariable(name = "postId") String postId){
        return new ResponseData(HttpStatus.OK.value(),likeService.getListUserLikedPost(postId));
    }
}
