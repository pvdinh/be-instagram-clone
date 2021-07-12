package com.example.demo.services;

import com.example.demo.models.Follow;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<Follow> findFollowByUserCurrent(){
        return followRepository.findFollowByUserCurrent(userAccountService.getUID());
    }
    public List<Follow> findFollowByUserFollowing(String uFId){
        return followRepository.findFollowByUserFollowing(uFId);
    }
    public String beginFollowing(String userFollowingId){
        try{
            followRepository.insert(new Follow("",userAccountService.getUID(),userFollowingId,System.currentTimeMillis()));
            return SUCCESS;
        }catch (Exception e){
            System.out.println("Duplicate");
            return FAIL;
        }
    }
    public String endFollowing(String userFollowingId){
        try{
            Follow follow=followRepository.findFollowsByUserCurrentAndUserFollowing(userAccountService.getUID(),userFollowingId);
            followRepository.delete(followRepository.findFollowsByUserCurrentAndUserFollowing(userAccountService.getUID(),userFollowingId));
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }
    public List<UserAccountSetting> suggestionsToFollow(){
        List<UserAccountSetting> userAccountSettings = new ArrayList<>();
        List<UserAccountSetting> userAccountSettingsFollowed = new ArrayList<>();
        List<Follow> follows = followRepository.findFollowByUserCurrent(userAccountService.getUID());
        follows.forEach(follow -> {
            userAccountSettingsFollowed.add(userAccountSettingRepository.findUserAccountSettingById(follow.getUserFollowing()));
        });
        userAccountSettings = userAccountSettingRepository.findAll();
        userAccountSettings.removeAll(userAccountSettingsFollowed);
        return userAccountSettings.size() < 5 ? userAccountSettings : userAccountSettings.subList(0,5);
    }

}
