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
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;
    @Autowired
    private UserAccountService userAccountService;

    public UserAccountSetting findUserAccountSettingByUsername(String username){
        return userAccountSettingRepository.findUserAccountSettingByUsername(username);
    }

    public UserAccountSetting findUserAccountSettingByJwt(){
        UserAccount userAccount = userAccountService.findUserAccountByUsernameOrEmailOrPhoneNumber(UsernameFromJWT.get());
        return userAccountSettingRepository.findUserAccountSettingByUsername(userAccount.getUsername());
    }

}
