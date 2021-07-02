package com.example.demo.services;

import com.example.demo.models.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserAccountService implements UserDetailsService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findUserAccountByUsernameOrEmailOrPhoneNumber(s,s,s);
        if(userAccount != null){
            return org.springframework.security.core.userdetails.User
                    .withUsername(userAccount.getUsername())
                    .password("{noop}"+userAccount.getPassword())
                    .authorities(Collections.emptyList())
                    .build();
        }else throw new NullPointerException("NOT FOUND : " + s);
    }

    public UserAccount findUserAccountByUsernameOrEmailOrPhoneNumber(String s){
        return userAccountRepository.findUserAccountByUsernameOrEmailOrPhoneNumber(s,s,s);
    }
}
