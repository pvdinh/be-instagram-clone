package com.example.demo.utils;

import com.example.demo.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationCurrentUser {
    @Autowired
    private UserAccountService userAccountService;

    public boolean checkCurrentUser(String id){
        return id.equals(userAccountService.getUID());
    }

}
