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
import org.springframework.web.bind.annotation.*;

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
            UserAccountSetting userAccountSetting = new UserAccountSetting(uAccount.getId(), userAccount.getDisplayName()
                    , "", 0, 0, 0, "https://res.cloudinary.com/dinhpv/image/upload/v1627739587/instargram-clone/avt_hcfwtt.png"
                    , uAccount.getUsername(), "");
            return new ResponseMessage(HttpStatus.OK.value(), userAccountSettingService.addUserAccountSetting(userAccountSetting));
        } else {
            return new ResponseMessage(HttpStatus.ACCEPTED.value(), resMess);
        }
    }

    @PostMapping("/{phone}/validatePhone")
    public BaseResponse validatePhone(@PathVariable(name = "phone") String phone){
        return userAccountService.validatePhone(phone) ? new ResponseMessage(HttpStatus.OK.value(),"error") : new ResponseMessage(HttpStatus.OK.value(),"success");
    }

    @PostMapping("/{email}/validateEmail")
    public BaseResponse validateEmail(@PathVariable(name = "email") String email){
        return userAccountService.validateEmail(email) ? new ResponseMessage(HttpStatus.OK.value(),"error") : new ResponseMessage(HttpStatus.OK.value(),"success");
    }

    @PostMapping("/{username}/validateUsername")
    public BaseResponse validateUsername(@PathVariable(name = "username") String username){
        return userAccountService.validateUsername(username) ? new ResponseMessage(HttpStatus.OK.value(),"error") : new ResponseMessage(HttpStatus.OK.value(),"success");
    }
}
