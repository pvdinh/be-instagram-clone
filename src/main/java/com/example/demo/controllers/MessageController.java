package com.example.demo.controllers;

import com.example.demo.models.message.Message;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.example.demo.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public BaseResponse findAll(){
        return new ResponseData(HttpStatus.OK.value(),messageService.findAllMessage());
    }

    @GetMapping("/sender")
    public BaseResponse findAllBySender(){
        return new ResponseData(HttpStatus.OK.value(),messageService.findAllMessageBySender());
    }

    @GetMapping("/{receiver}/receiver")
    public BaseResponse findAllBySenderAndReceiver(@PathVariable(name = "receiver") String receiver){
        return new ResponseObject<>(HttpStatus.OK.value(),messageService.findAllMessageBySenderAndReceiver(receiver));
    }

    @PostMapping
    public BaseResponse postMessage(@RequestBody Message message){
        return new ResponseMessage(HttpStatus.OK.value(),messageService.postMessage(message));
    }
}
