package com.example.demo.services;

import com.example.demo.models.Follow;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.models.message.Message;
import com.example.demo.models.message.MessageInformation;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountSettingService userAccountSettingService;
    @Autowired
    private FollowRepository followRepository;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<Message> findAllMessage(){
        return messageRepository.findAll();
    }

    //lấy ra danh sách các người nhận hoặc gửi
    public List<String> listReceiver(){
        List<String> listReceiver=new ArrayList<>();
        //danh sách người gửi đã được phản hồi
        messageRepository.findMessagesBySender(userAccountService.getUID()).forEach(message -> {
            if(!listReceiver.contains(message.getReceiver())){
                listReceiver.add(message.getReceiver());
            }
        });
        //khi người nhận chưa phản hồi người gửi
        //nếu thiếu, chỉ hiện thị danh sách mà người nhận đã trả lời
        messageRepository.findMessagesByReceiver(userAccountService.getUID()).forEach(message -> {
            if(!listReceiver.contains(message.getSender())){
                listReceiver.add(message.getSender());
            }
        });
        return listReceiver;
    }

    public List<MessageInformation> findAllMessageBySender(){
        List<MessageInformation> messageInformations = new ArrayList<>();
        listReceiver().forEach(s -> {
            List<Message> messages = messageRepository.findMessagesBySenderAndReceiver(s,userAccountService.getUID());
            messages.addAll(messageRepository.findMessagesBySenderAndReceiver(userAccountService.getUID(),s));
            Collections.sort(messages);
            messageInformations.add(new MessageInformation(userAccountSettingService.findUserAccountSettingById(s)
                    ,messages.size() > 10 ? messages.subList(messages.size()-10,messages.size()) : messages));
        });
        Collections.sort(messageInformations);
        return messageInformations;
    }

    public MessageInformation findAllMessageBySenderAndReceiver(String receiver,int page,int size){
        List<Message> messages = messageRepository.findMessagesBySenderAndReceiver(receiver,userAccountService.getUID());
        messages.addAll(messageRepository.findMessagesBySenderAndReceiver(userAccountService.getUID(),receiver));
        Collections.sort(messages);
        UserAccountSetting userAccountSettingReceiver = userAccountSettingService.findUserAccountSettingById(receiver);
        if((page * size) > messages.size()){
            return new MessageInformation(userAccountSettingReceiver,Collections.EMPTY_LIST);
        }else {
            PagedListHolder pageable = new PagedListHolder(messages);
            pageable.setPageSize(size);
            pageable.setPage(page);
            return new MessageInformation(userAccountSettingReceiver,pageable.getPageList());
        }
    }

    public String postMessage(Message message){
        message.setDateSendMessage(System.currentTimeMillis());
        if(userAccountSettingService.findUserAccountSettingById(message.getReceiver()) != null){
            messageRepository.insert(message);
            return SUCCESS;
        }else {
            System.out.println("RECEIVER NOT EXISTS");
            return FAIL;
        }
    }

    public List<UserAccountSetting> findReceiverByUsername(String search){
        List<UserAccountSetting> userAccountSettings = new ArrayList<>();
        List<Follow> follows =followRepository.findFollowByUserCurrent(userAccountService.getUID());
        follows.forEach(follow -> {
            UserAccountSetting userAccountSetting = userAccountSettingService.findUserAccountSettingById(follow.getUserFollowing());
            if(userAccountSetting.getUsername().contains(search)){
                userAccountSettings.add(userAccountSetting);
            }
        });
        if(userAccountSettings.size() < 50){
            userAccountSettingService.findUserAccountSettingsByUsernameContains(search).forEach(userAccountSetting -> {
                if(!userAccountSettings.contains(userAccountSetting)){
                    userAccountSettings.add(userAccountSetting);
                }
            });
        }
        return userAccountSettings.size() > 49 ? userAccountSettings.subList(0,50) : userAccountSettings;
    }

    public void deleteBySender(String idUSer){
        messageRepository.deleteBySender(idUSer);
    }
    public void deleteByReceiver(String idUSer){
        messageRepository.deleteByReceiver(idUSer);
    }

}
