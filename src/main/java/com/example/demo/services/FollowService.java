package com.example.demo.services;

import com.example.demo.models.Follow;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.models.activity.Activity;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;
    @Autowired
    private ActivityService activityService;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<Follow> findFollowByUserCurrent(){
        return followRepository.findFollowByUserCurrent(userAccountService.getUID());
    }

    public List<UserAccountSetting> findFollowingByUserCurrent(String uCId,int page,int size){
        List<UserAccountSetting> userAccountSettings = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page,size);
            followRepository.findFollowByUserCurrent(uCId,pageable).forEach(follow -> {
                userAccountSettings.add(userAccountSettingRepository.findUserAccountSettingById(follow.getUserFollowing()));
            });
            return userAccountSettings;
        }catch (Exception e){
            return userAccountSettings;
        }
    }

    public List<UserAccountSetting> findFollowersByUserCurrent(String uFId,int page,int size){
        List<UserAccountSetting> userAccountSettings = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page,size);
            followRepository.findFollowByUserFollowing(uFId,pageable).forEach(follow -> {
                userAccountSettings.add(userAccountSettingRepository.findUserAccountSettingById(follow.getUserCurrent()));
            });
            return userAccountSettings;
        }catch (Exception e){
            return userAccountSettings;
        }
    }

    public List<Follow> findFollowByUserFollowing(String uFId){
        return followRepository.findFollowByUserFollowing(uFId);
    }
    public String insert(String id){
        try{
            followRepository.insert(new Follow("",id,id,System.currentTimeMillis()));
            return SUCCESS;
        }catch (Exception e){
            System.out.println("Duplicate");
            return FAIL;
        }
    }
    public String beginFollowing(String userFollowingId){
        try{
            followRepository.insert(new Follow("",userAccountService.getUID(),userFollowingId,System.currentTimeMillis()));
            updateFollow(userFollowingId,userAccountService.getUID());
            //thêm vào activity
            activityService.insert(new Activity(userAccountService.getUID(),userFollowingId,"","follow",0,System.currentTimeMillis()));
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
            //xoá khỏi activity
            Activity activity = activityService.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivity(userAccountService.getUID(),userFollowingId,"follow");
            activityService.delete(activity);
            updateFollow(userFollowingId,userAccountService.getUID());
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public String removeFollowing(String id){
        try{
            followRepository.delete(followRepository.findFollowsByUserCurrentAndUserFollowing(id,userAccountService.getUID()));
            //xoá khỏi activity
            Activity activity = activityService.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivity(id,userAccountService.getUID(),"follow");
            activityService.delete(activity);
            updateFollow(userAccountService.getUID(),id);
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public void updateFollow(String userFollowingId,String cUId){
        //Cập nhật lại số người theo dõi của bên thứ 2(tăng hoặc giảm 1)
        //trong TH beginFollow : sẽ tăng lên 1, Follower của bên thứ 2
        UserAccountSetting userAccountSettingFollowing = userAccountSettingRepository.findUserAccountSettingById(userFollowingId);
        userAccountSettingFollowing.setFollowers(followRepository.findFollowByUserFollowing(userFollowingId).size());
        userAccountSettingRepository.save(userAccountSettingFollowing);

        //cập nhật lại số người đang theo dõi của current user(tăng hoặc giảm 1)
        //trong TH beginFollow : sẽ tăng lên 1, Following của người dùng hiện tại
        UserAccountSetting userAccountSettingFollower = userAccountSettingRepository.findUserAccountSettingById(cUId);
        userAccountSettingFollower.setFollowing(followRepository.findFollowByUserCurrent(cUId).size());
        userAccountSettingRepository.save(userAccountSettingFollower);
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

    public boolean checkFollow(String uFId){
        try {
            Follow follow = followRepository.findFollowsByUserCurrentAndUserFollowing(userAccountService.getUID(),uFId);
            if(follow != null){
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

}
