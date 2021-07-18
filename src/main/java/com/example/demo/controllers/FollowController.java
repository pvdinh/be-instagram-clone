package com.example.demo.controllers;

import com.example.demo.models.Follow;
import com.example.demo.repository.FollowRepository;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @GetMapping
    public BaseResponse findFollowByUserCurrent(){
        return new ResponseData(HttpStatus.OK.value(),followService.findFollowByUserCurrent());
    }

    @GetMapping("/suggestions")
    public BaseResponse suggestionsToFollow(){
        return new ResponseData(HttpStatus.OK.value(),followService.suggestionsToFollow());
    }

    @PostMapping("/{userFollowingId}/begin")
    public BaseResponse beginFollowing(@PathVariable(name = "userFollowingId") String userFollowingId){
        return new ResponseMessage(HttpStatus.OK.value(),followService.beginFollowing(userFollowingId));
    }

    @DeleteMapping("/{userFollowingId}/end")
    public BaseResponse endFollowing(@PathVariable(name = "userFollowingId") String userFollowingId){
        return new ResponseMessage(HttpStatus.OK.value(),followService.endFollowing(userFollowingId));
    }

}
