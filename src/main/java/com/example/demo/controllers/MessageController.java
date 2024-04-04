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

import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public BaseResponse findAll(){
        try{
            return new ResponseData(HttpStatus.OK.value(),messageService.findAllMessage());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @GetMapping("/sender")
    public BaseResponse findAllBySender(){
        try{
            return new ResponseData(HttpStatus.OK.value(),messageService.findAllMessageBySender());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @GetMapping("/{receiver}/receiver")
    public BaseResponse findAllBySenderAndReceiver(@PathVariable(name = "receiver") String receiver, @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size){
        try{
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseObject<>(HttpStatus.OK.value(),messageService.findAllMessageBySenderAndReceiver(receiver,currentPage,pageSize));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @PostMapping
    public BaseResponse postMessage(@RequestBody Message message){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),messageService.postMessage(message));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    @PostMapping("/{search}/suggested")
    public BaseResponse findReceiverByUsername(@PathVariable(name = "search") String search){
        try{
            return new ResponseData(HttpStatus.OK.value(), messageService.findReceiverByUsername(search));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }
}
