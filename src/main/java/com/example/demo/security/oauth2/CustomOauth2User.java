package com.example.demo.security.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomOauth2User implements OAuth2User {
    private OAuth2User oAuth2User;

    public CustomOauth2User() {
        super();
    }

    public CustomOauth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }
    public String getEmail(){
        return oAuth2User.getAttribute("email");
    }
    public String getId(){
        return oAuth2User.getAttribute("id");
    }
    public String getPicture(){
        HashMap hashMap = (HashMap) oAuth2User.getAttribute("picture");
        HashMap hashMapUrl = (HashMap) hashMap.get("data");
        return (String) hashMapUrl.get("url");
    }
    public String getHometown(){
        HashMap hashMap = (HashMap) oAuth2User.getAttribute("hometown");
        return (String) hashMap.get("name");
    }
    public String getBirthday(){
        return oAuth2User.getAttribute("birthday");
    }
}
