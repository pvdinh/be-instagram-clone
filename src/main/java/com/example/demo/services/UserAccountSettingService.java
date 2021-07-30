package com.example.demo.services;

import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import com.example.demo.utils.UsernameFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountSettingService {
    private final String SUCCESS = "success";
    private final String FAIL = "fail";
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private FollowService followService;

    public UserAccountSetting findUserAccountSettingByUsername(String username){
        return userAccountSettingRepository.findUserAccountSettingByUsername(username);
    }
    public UserAccountSetting findUserAccountSettingById(String id){
        return userAccountSettingRepository.findUserAccountSettingById(id);
    }

    public UserAccountSetting findUserAccountSettingByJwt(){
        UserAccount userAccount = userAccountService.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(UsernameFromJWT.get());
        return userAccountSettingRepository.findUserAccountSettingByUsername(userAccount.getUsername());
    }

    public String addUserAccountSetting(UserAccountSetting userAccountSetting){
        try{
            userAccountSettingRepository.insert(userAccountSetting);
            followService.insert(userAccountSetting.getId());
            return SUCCESS;
        }catch (Exception e){
            System.out.println("Account EXISTS");
            return FAIL;
        }
    }
}
