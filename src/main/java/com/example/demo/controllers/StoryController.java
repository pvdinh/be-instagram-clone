package com.example.demo.controllers;

import com.example.demo.models.home.Story;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/story")
public class StoryController {
    @Autowired
    private StoryService storyService;

    @GetMapping
    public BaseResponse getAllStoryFollowing(){
        try {
            return new ResponseData(HttpStatus.OK.value(),storyService.getAllStoryFollowing());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }
    @PostMapping
    public BaseResponse beginStory(@RequestBody Story story){
        try {
            return new ResponseMessage(HttpStatus.OK.value(),storyService.beginStory(story));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }
    @DeleteMapping("{idStory}/end")
    public BaseResponse endStory(@PathVariable(name = "idStory") String idStory){
        try {
            return new ResponseMessage(HttpStatus.OK.value(),storyService.endStory(idStory));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }
}
