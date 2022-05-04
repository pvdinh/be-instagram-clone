package com.example.demo.models.group;

import com.example.demo.models.UserAccountSetting;

public class MemberInGroup {
    private GroupMember groupMember;
    private UserAccountSetting userAccountSetting;
    private UserAccountSetting userInvite;

    public MemberInGroup() {
        super();
    }

    public MemberInGroup(GroupMember groupMember, UserAccountSetting userAccountSetting, UserAccountSetting userInvite) {
        this.groupMember = groupMember;
        this.userAccountSetting = userAccountSetting;
        this.userInvite = userInvite;
    }

    public GroupMember getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(GroupMember groupMember) {
        this.groupMember = groupMember;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }

    public UserAccountSetting getUserInvite() {
        return userInvite;
    }

    public void setUserInvite(UserAccountSetting userInvite) {
        this.userInvite = userInvite;
    }
}
