package com.example.demo.filters;

import com.example.demo.models.Account;
import com.example.demo.security.oauth2.CustomOauth2User;
import com.example.demo.utils.ConvertSHA1;
import com.example.demo.utils.TokenAuthentication;
import com.google.gson.Gson;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JwtLoginFilter(String url, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        BufferedReader reader = httpServletRequest.getReader();
        Gson gson = new Gson();
        Account account = gson.fromJson(reader, Account.class);
//        CustomOauth2User customOauth2User = new CustomOauth2User();
//        try {
//            customOauth2User = (CustomOauth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            Authentication authentication = getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
//                    customOauth2User.getId(),
//                    ConvertSHA1.convertSHA1(customOauth2User.getId()),
//                    Collections.emptyList()
//            ));
//            return authentication;
//        }catch (NullPointerException e){
//            System.out.println("Not login Facebook");
//        }

        Authentication authentication = getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                account.getUsername(),
                ConvertSHA1.convertSHA1(account.getPassword()),
                Collections.emptyList()
        ));
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        TokenAuthentication.addAuthentication(response,authResult.getName());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        TokenAuthentication.failAuthentication(response);
    }
}
