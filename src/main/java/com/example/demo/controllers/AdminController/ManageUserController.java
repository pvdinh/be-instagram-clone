package com.example.demo.controllers.AdminController;

import com.example.demo.models.UserAccount;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.example.demo.services.UserAccountService;
import com.example.demo.services.UserAccountSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/manage-user")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ManageUserController {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountSettingService userAccountSettingService;

    //http://localhost:8080/api/v1/admin/manage-user
    @GetMapping
    public BaseResponse findAllPageable(@RequestParam(name = "page")Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size){
        try{
            int currentPage = page.orElse(0);
            int pageSize= page.orElse(10);
            List<UserAccount> userAccounts = userAccountService.findAllPageable(currentPage,pageSize);
            return new ResponseData(HttpStatus.OK.value(),userAccounts,userAccountService.findAll().size());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-user/{uId}
    @GetMapping("/{uId}")
    public BaseResponse findById(@PathVariable(name = "uId") String uId){
        try{
            return new ResponseObject(HttpStatus.OK.value(),userAccountService.findUserAccountById(uId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-user/{uId}/user-account-setting
    @GetMapping("/{uId}/user-account-setting")
    public BaseResponse findUserAccountSettingById(@PathVariable(name = "uId") String uId){
        try{
            return new ResponseObject(HttpStatus.OK.value(),userAccountSettingService.findUserAccountSettingById(uId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

}
