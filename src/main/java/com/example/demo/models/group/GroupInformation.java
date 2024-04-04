package com.example.demo.models.group;

import com.example.demo.models.UserAccountSetting;

public class GroupInformation {
    private Group group;
    private GroupMember groupMember;

    public GroupInformation() {
        super();
    }

    public GroupInformation(Group group, GroupMember groupMember) {
        this.group = group;
        this.groupMember = groupMember;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public GroupMember getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(GroupMember groupMember) {
        this.groupMember = groupMember;
    }
}
