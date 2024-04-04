package com.example.demo.controllers;

import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.ExploreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/explore")
public class ExploreController {
    @Autowired
    private ExploreService exploreService;

    @GetMapping()
    public BaseResponse getExplorePosts(@RequestParam(name = "page") int page,@RequestParam(name = "size") int size ){
        try{
            return new ResponseData(HttpStatus.OK.value(),exploreService.getExplorePosts(page,size));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }
}
