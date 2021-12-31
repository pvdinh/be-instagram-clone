package com.example.demo.models.message;

import com.example.demo.models.UserAccountSetting;

import java.util.List;

public class MessageInformation implements Comparable<MessageInformation>{
    private UserAccountSetting userAccountSettingReceiver;
    private List<Message> messages;

    public MessageInformation() {
        super();
    }

    public MessageInformation(UserAccountSetting userAccountSettingReceiver, List<Message> messages) {
        this.userAccountSettingReceiver = userAccountSettingReceiver;
        this.messages = messages;
    }

    public UserAccountSetting getUserAccountSettingReceiver() {
        return userAccountSettingReceiver;
    }

    public void setUserAccountSettingReceiver(UserAccountSetting userAccountSettingReceiver) {
        this.userAccountSettingReceiver = userAccountSettingReceiver;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int compareTo(MessageInformation o) {
        return Long.compare(o.messages.get(o.messages.size()-1).getDateSendMessage(),this.messages.get(this.messages.size()-1).getDateSendMessage());
    }
}
