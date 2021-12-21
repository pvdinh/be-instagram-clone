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
        try{
            return new ResponseData(HttpStatus.OK.value(),followService.findFollowByUserCurrent());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @GetMapping("/suggestions")
    public BaseResponse suggestionsToFollow(){
        try{
            return new ResponseData(HttpStatus.OK.value(),followService.suggestionsToFollow());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @PostMapping("/{userFollowingId}/begin")
    public BaseResponse beginFollowing(@PathVariable(name = "userFollowingId") String userFollowingId){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),followService.beginFollowing(userFollowingId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @DeleteMapping("/{userFollowingId}/end")
    public BaseResponse endFollowing(@PathVariable(name = "userFollowingId") String userFollowingId){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),followService.endFollowing(userFollowingId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

}
