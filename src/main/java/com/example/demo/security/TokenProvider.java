package com.example.demo.security;

import com.example.demo.security.oauth2.CustomOauth2User;
import com.example.demo.utils.TokenAuthentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenProvider {

    public String createToken(Authentication authentication) {
        CustomOauth2User customOauth2User = (CustomOauth2User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(customOauth2User.getName())
                .setExpiration(new Date(System.currentTimeMillis() + TokenAuthentication.EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, TokenAuthentication.SECRET)
                .compact();
    }
}