package com.example.demo.controllers.SearchInLayout;

import com.example.demo.models.SearchInLayout.HistorySearchUser;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.HistorySearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/history-search-user")
public class HistorySearchUserController {
    @Autowired
    private HistorySearchUserService historySearchUserService;

    @GetMapping
    public BaseResponse findAll(){
        try {
            return new ResponseData(HttpStatus.OK.value(),historySearchUserService.findAll());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @PostMapping
    public BaseResponse insert(@RequestBody HistorySearchUser historySearchUser){
        try {
            return new ResponseMessage(HttpStatus.OK.value(),historySearchUserService.insert(historySearchUser));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }
}
