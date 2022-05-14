package com.example.demo.models.SearchInLayout;

import com.example.demo.models.UserAccountSetting;

public class HistorySearchUserResult {
    private HistorySearchUser historySearchUser;
    private UserAccountSetting userAccountSetting;

    public HistorySearchUserResult() {
        super();
    }

    public HistorySearchUserResult(HistorySearchUser historySearchUser, UserAccountSetting userAccountSetting) {
        this.historySearchUser = historySearchUser;
        this.userAccountSetting = userAccountSetting;
    }

    public HistorySearchUser getHistorySearchUser() {
        return historySearchUser;
    }

    public void setHistorySearchUser(HistorySearchUser historySearchUser) {
        this.historySearchUser = historySearchUser;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }
}
