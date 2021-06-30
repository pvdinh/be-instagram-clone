package com.example.demo.services;

import com.example.demo.models.UserAccountSetting;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountSettingService {
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;

    public UserAccountSetting findUserAccountSettingByUsername(String username){
        return userAccountSettingRepository.findUserAccountSettingByUsername(username);
    }

}
