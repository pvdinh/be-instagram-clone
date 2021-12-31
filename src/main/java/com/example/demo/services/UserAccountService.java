package com.example.demo.services;

import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.utils.UsernameFromJWT;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAccountService implements UserDetailsService {
    private final String SUCCESS = "success";
    private final String FAIL = "fail";
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(s,s,s,s);
        List<SimpleGrantedAuthority> grantedAuthorityList = userAccount.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        if(userAccount != null){
            return org.springframework.security.core.userdetails.User
                    .withUsername(userAccount.getUsername())
                    .password("{noop}"+userAccount.getPassword())
                    .authorities(grantedAuthorityList)
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
        if(isNumeric(s)){
            UserAccount userAccount = userAccountRepository.findUserAccountByPhoneNumber(s);
            return userAccount!= null ? true : false;
        }else {
            return true;
        }
    }
    public boolean validateEmail(String s){
        try {
            UserAccount userAccount = userAccountRepository.findUserAccountByEmail(s);
            return userAccount!= null ? true : false;
        }catch (Exception e){
            return true;
        }
    }
    public boolean validateUsername(String s){
        try {
            UserAccount userAccount = userAccountRepository.findUserAccountByUsername(s);
            return userAccount!= null ? true : false;
        }catch (Exception e){
            return true;
        }
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
