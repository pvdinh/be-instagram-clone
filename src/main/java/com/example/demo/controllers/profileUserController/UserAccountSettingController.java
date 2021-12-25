package com.example.demo.controllers.profileUserController;

import com.example.demo.models.UserAccountSetting;
import com.example.demo.models.profile.Profile;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.example.demo.services.FollowService;
import com.example.demo.services.SavedPostService;
import com.example.demo.services.StoryService;
import com.example.demo.services.UserAccountSettingService;
import com.example.demo.utils.UsernameFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/user-account-setting")
public class UserAccountSettingController {

    @Autowired
    private UserAccountSettingService userAccountSettingService;
    @Autowired
    private SavedPostService savedPostService;
    @Autowired
    private FollowService followService;
    @Autowired
    private StoryService storyService;

    @GetMapping("/{username}")
    public BaseResponse findUserAccountSettingByUsername(@PathVariable(name = "username") String username) {
        UserAccountSetting userAccountSetting = userAccountSettingService.findUserAccountSettingByUsername(username);
        return userAccountSetting != null ? new ResponseObject(HttpStatus.OK.value(), userAccountSetting)
                : new ResponseMessage(HttpStatus.ACCEPTED.value(), "not found ");
    }
    @PostMapping("/{username}/profile")
    public BaseResponse findUserProfileByUsername(@PathVariable(name = "username") String username) {
        Profile profile=userAccountSettingService.findUserProfileByUsername(username);
        if(profile == null){
            return new ResponseMessage(HttpStatus.NOT_FOUND.value(),"not found user !");
        }else return new ResponseObject(HttpStatus.OK.value(),profile);
    }

    @GetMapping("/get")
    public BaseResponse findUserAccountSettingByJwt(){
        UserAccountSetting userAccountSetting = userAccountSettingService.findUserAccountSettingByJwt();
        return userAccountSetting != null ? new ResponseObject(HttpStatus.OK.value(),userAccountSetting)
                : new ResponseMessage(HttpStatus.ACCEPTED.value(),"not found ");
    }

    @PostMapping("/edit")
    public BaseResponse editUserAccountSetting(@RequestBody HashMap<String,String> data){
        try {
            return new ResponseMessage(HttpStatus.OK.value(),userAccountSettingService.editUserAccountSetting(data));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @GetMapping("/get-private-info")
    public BaseResponse getPrivateInfo(){
        try{
            return new ResponseObject(HttpStatus.OK.value(),userAccountSettingService.getPrivateInfo());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @PostMapping("/change-password")
    public BaseResponse changePassword(@RequestBody HashMap<String,String> data){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),userAccountSettingService.changePassword(data));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @PostMapping("/change-profile-photo")
    public BaseResponse changeProfilePhoto(@RequestBody HashMap<String,String> data){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),userAccountSettingService.changeProfilePhoto(data));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @GetMapping("/{username}/get-saved-post")
    public BaseResponse getSavedPost(@PathVariable(name = "username") String username){
        try{
            if(UsernameFromJWT.get().equals(username)){
                return new ResponseData(HttpStatus.OK.value(),savedPostService.getListPostSavedFromUID());
            }else {
                return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
            }
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @GetMapping("/{userFollowingId}/checkFollow")
    public BaseResponse checkFollow(@PathVariable(name = "userFollowingId") String userFollowingId){
        try{
            return new ResponseObject(HttpStatus.OK.value(),followService.checkFollow(userFollowingId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @GetMapping("/{userId}/checkStory")
    public BaseResponse checkStory(@PathVariable(name = "userId") String userId){
        try{
            return new ResponseObject(HttpStatus.OK.value(),storyService.checkStory(userId));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

}
