package com.example.demo.controllers;

import com.example.demo.models.AuthProvider;
import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.UserAccountService;
import com.example.demo.services.UserAccountSettingService;
import com.example.demo.utils.ConvertSHA1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountSettingService userAccountSettingService;

    @PostMapping("/register")
    public BaseResponse register(@RequestBody UserAccount userAccount) {
        userAccount.setPassword(ConvertSHA1.convertSHA1(userAccount.getPassword()));
        userAccount.setAuthProvider(AuthProvider.local);
        String resMess = userAccountService.addUserAccount(userAccount);
        if (resMess.equalsIgnoreCase("success")) {
            UserAccount uAccount = userAccountService.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(userAccount.getUsername());
            UserAccountSetting userAccountSetting = new UserAccountSetting(uAccount.getId(), uAccount.getUsername()
                    , "", "0", "0", "0", "https://res.cloudinary.com/dinhpv/image/upload/v1625041460/instargram-clone/no_avatar_p8konr.png"
                    , uAccount.getUsername(), "");
            return new ResponseMessage(HttpStatus.OK.value(), userAccountSettingService.addUserAccountSetting(userAccountSetting));
        } else {
            return new ResponseMessage(HttpStatus.ACCEPTED.value(), resMess);
        }
    }
}
