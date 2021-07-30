package com.example.demo.services;

import com.example.demo.models.message.Message;
import com.example.demo.models.message.MessageInformation;
import com.example.demo.repository.MessageRepository;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountSettingService userAccountSettingService;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<Message> findAllMessage(){
        return messageRepository.findAll();
    }

    public List<String> listReceiver(){
        List<String> listReceiver=new ArrayList<>();
        messageRepository.findMessagesBySender(userAccountService.getUID()).forEach(message -> {
            if(!listReceiver.contains(message.getReceiver())){
                listReceiver.add(message.getReceiver());
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
                    ,messages));
        });
        return messageInformations;
    }

    public MessageInformation findAllMessageBySenderAndReceiver(String receiver){
        List<Message> messages = messageRepository.findMessagesBySenderAndReceiver(receiver,userAccountService.getUID());
        messages.addAll(messageRepository.findMessagesBySenderAndReceiver(userAccountService.getUID(),receiver));
        Collections.sort(messages);
        return new MessageInformation(userAccountSettingService.findUserAccountSettingById(receiver),messages);
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
}
