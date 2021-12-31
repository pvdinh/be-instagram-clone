package com.example.demo.controllers;

import com.example.demo.models.activity.Activity;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping
    public BaseResponse findAllByIdInteractUser(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        try {
            return new ResponseData(HttpStatus.OK.value(), activityService.findAll(page, size));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }


    @PutMapping
    public BaseResponse updateActivity(@RequestBody Activity activity) {
        try {
            return new ResponseMessage(HttpStatus.OK.value(), activityService.update(activity));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

}
