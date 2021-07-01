package com.example.demo.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class UsernameFromJWT {
    public static String get(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
