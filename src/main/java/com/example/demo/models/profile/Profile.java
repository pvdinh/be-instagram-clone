package com.example.demo.models.profile;

import com.example.demo.models.UserAccountSetting;

import java.util.List;

public class Profile {
    private UserAccountSetting userAccountSetting;
    private List<PostDetail> postDetails;

    public Profile() {
        super();
    }

    public Profile(UserAccountSetting userAccountSetting, List<PostDetail> postDetails) {
        this.userAccountSetting = userAccountSetting;
        this.postDetails = postDetails;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }

    public List<PostDetail> getPostDetails() {
        return postDetails;
    }

    public void setPostDetails(List<PostDetail> postDetails) {
        this.postDetails = postDetails;
    }
}
