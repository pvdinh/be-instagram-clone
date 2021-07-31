package com.example.demo.controllers.webSocketController;

import com.example.demo.models.message.Message;
import com.example.demo.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageWebSocketController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("chat.sendMessage")
    @SendTo("/inbox/public")
    public Message sendMessage(@Payload Message message){
        messageService.postMessage(message);
        return message;
    }
}
