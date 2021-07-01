package com.example.demo.controllers;

import com.example.demo.models.UserAccountSetting;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.example.demo.services.UserAccountSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-account-setting")
public class UserAccountSettingController {

    @Autowired
    private UserAccountSettingService userAccountSettingService;

    @GetMapping("/{username}")
    public BaseResponse findUserAccountSettingByUsername(@PathVariable(name = "username") String username) {
        UserAccountSetting userAccountSetting = userAccountSettingService.findUserAccountSettingByUsername(username);
        return userAccountSetting != null ? new ResponseObject(HttpStatus.OK.value(), userAccountSetting)
                : new ResponseMessage(HttpStatus.ACCEPTED.value(), "not found ");
    }

    @GetMapping("/get")
    public BaseResponse findUserAccountSettingByJwt(){
        UserAccountSetting userAccountSetting = userAccountSettingService.findUserAccountSettingByJwt();
        return userAccountSetting != null ? new ResponseObject(HttpStatus.OK.value(),userAccountSetting)
                : new ResponseMessage(HttpStatus.ACCEPTED.value(),"not found ");
    }

}
