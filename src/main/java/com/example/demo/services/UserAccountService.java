package com.example.demo.services;

import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.utils.UsernameFromJWT;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserAccountService implements UserDetailsService {
    private final String SUCCESS = "success";
    private final String FAIL = "fail";
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(s,s,s,s);
        if(userAccount != null){
            return org.springframework.security.core.userdetails.User
                    .withUsername(userAccount.getUsername())
                    .password("{noop}"+userAccount.getPassword())
                    .authorities(Collections.emptyList())
                    .build();
        }else throw new NullPointerException("NOT FOUND : " + s);
    }

    public UserAccount findUserAccountByUsernameOrEmailOrPhoneNumberOrId(String s){
        return userAccountRepository.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(s,s,s,s);
    }

    public String getUID(){
        return userAccountRepository.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(UsernameFromJWT.get(),UsernameFromJWT.get(),UsernameFromJWT.get(),UsernameFromJWT.get()).getId();
    }

    public String addUserAccount(UserAccount userAccount){
        try{
            userAccountRepository.insert(userAccount);
            return SUCCESS;
        }catch (Exception e){
            userAccountRepository.save(userAccount);
            System.out.println("UPDATE ACCOUNT EXISTS");
            return FAIL;
        }
    }
    public boolean validatePhone(String s){
        UserAccount userAccount = userAccountRepository.findUserAccountByPhoneNumber(s);
        return userAccount!= null ? true : false;
    }
    public boolean validateEmail(String s){
        UserAccount userAccount = userAccountRepository.findUserAccountByEmail(s);
        return userAccount!= null ? true : false;
    }
    public boolean validateUsername(String s){
        UserAccount userAccount = userAccountRepository.findUserAccountByUsername(s);
        return userAccount!= null ? true : false;
    }
}
