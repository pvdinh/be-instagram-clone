package com.example.demo.services;

import com.example.demo.models.Follow;
import com.example.demo.models.Post;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.models.home.Story;
import com.example.demo.models.profile.PostDetail;
import com.example.demo.models.profile.Profile;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.StoryRepository;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StoryService {
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private FollowService  followService;
    @Autowired
    private UserAccountSettingService  userAccountSettingService;
    @Autowired
    private PostRepository postRepository;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<Profile> getAllStoryFollowing(){
        List<Profile> profiles = new ArrayList<>();
        List<UserAccountSetting> userAccountSettings = new ArrayList<>();
        //lấy danh sách following và userAccountSetting
        List<Follow> follows = followService.findFollowByUserCurrent();
        follows.forEach(follow -> {
            UserAccountSetting userAccountSetting = userAccountSettingService.findUserAccountSettingById(follow.getUserFollowing());
            userAccountSettings.add(userAccountSettingService.findUserAccountSettingById(follow.getUserFollowing()));
        });
        //--------------------------------------------
        userAccountSettings.forEach(userAccountSetting -> {
            List<Story> stories = storyRepository.findStoryByIdUser(userAccountSetting.getId());
            if(stories.size() > 0){
                List<PostDetail> postDetails = new ArrayList<>();
                stories.forEach(story -> {
                    if(story.getDateEnd() > System.currentTimeMillis()){
                        postDetails.add(new PostDetail(postRepository.findPostById(story.getIdPost()),0, Collections.emptyList(),story.getDateStart()));
                    }
                });
                if(postDetails.size() > 0){
                    profiles.add(new Profile(userAccountSetting,postDetails));
                }
            }
        });
        return profiles;
    }

    public String beginStory(Story story){
        try {
            //Mỗi story sẽ được hiển thị trong 1 ngày
            story.setDateStart(System.currentTimeMillis());
            story.setDateEnd(story.getDateStart() + (24*60*60*1000));
            storyRepository.insert(story);
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public String endStory(String idStory){
        try {
            Story story = storyRepository.findStoryById(idStory);
            if(story != null && userAccountService.getUID() == story.getIdUser()){
                storyRepository.delete(story);
                return SUCCESS;
            }
            else return FAIL;
        }catch (Exception e){
            return FAIL;
        }
    }

    public boolean checkStory(String uFId){
        try{
            List<Story> stories = new ArrayList<>();
            storyRepository.findStoryByIdUser(uFId).forEach(story -> {
                if (story.getDateEnd() > System.currentTimeMillis()){
                    stories.add(story);
                }
            });
            if(stories.size() > 0){
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
