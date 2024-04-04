package com.example.demo.models.adminModels;

import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;

public class UserAccountProfile {
    private UserAccount userAccount;
    private UserAccountSetting userAccountSetting;

    public UserAccountProfile() {
        super();
    }

    public UserAccountProfile(UserAccount userAccount, UserAccountSetting userAccountSetting) {
        this.userAccount = userAccount;
        this.userAccountSetting = userAccountSetting;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }
}
