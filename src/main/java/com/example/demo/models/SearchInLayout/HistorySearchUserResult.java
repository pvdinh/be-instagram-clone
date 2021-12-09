package com.example.demo.models.SearchInLayout;

import com.example.demo.models.UserAccountSetting;

public class HistorySearchUserResult {
    private long dateSearch;
    private UserAccountSetting userAccountSetting;

    public HistorySearchUserResult() {
        super();
    }

    public HistorySearchUserResult(long dateSearch, UserAccountSetting userAccountSetting) {
        this.dateSearch = dateSearch;
        this.userAccountSetting = userAccountSetting;
    }

    public long getDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(long dateSearch) {
        this.dateSearch = dateSearch;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }

}
