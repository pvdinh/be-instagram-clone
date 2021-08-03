package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.models.profile.PostDetail;
import com.example.demo.models.profile.Profile;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import com.example.demo.utils.SortClassCustom;
import com.example.demo.utils.UsernameFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAccountSettingService {
    private final String SUCCESS = "success";
    private final String FAIL = "fail";
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private FollowService followService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    public UserAccountSetting findUserAccountSettingByUsername(String username){
        return userAccountSettingRepository.findUserAccountSettingByUsername(username);
    }
    public UserAccountSetting findUserAccountSettingById(String id){
        return userAccountSettingRepository.findUserAccountSettingById(id);
    }
    public List<UserAccountSetting> findUserAccountSettingsByUsernameContains(String search){
        return userAccountSettingRepository.findUserAccountSettingsByUsernameContains(search);
    }

    public UserAccountSetting findUserAccountSettingByJwt(){
        UserAccount userAccount = userAccountService.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(UsernameFromJWT.get());
        return userAccountSettingRepository.findUserAccountSettingByUsername(userAccount.getUsername());
    }

    public String addUserAccountSetting(UserAccountSetting userAccountSetting){
        try{
            userAccountSettingRepository.insert(userAccountSetting);
            followService.insert(userAccountSetting.getId());
            return SUCCESS;
        }catch (Exception e){
            System.out.println("Account EXISTS");
            return FAIL;
        }
    }

    public Profile findUserProfileByUsername(String username){
        UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingByUsername(username);
        if(userAccountSetting!=null){
            List<PostDetail> postDetails=new ArrayList<>();
            List<Post> posts = postService.findAllByUserId(userAccountSetting.getId());
            posts.forEach(post -> {
                List<String> listLikeString=likeService.getListUserLikedPost(post.getId());
                int numberOfComments=commentService.findCommentByIdPost(post.getId()).size();
                postDetails.add(new PostDetail(post,numberOfComments,listLikeString));
            });
            postDetails.sort(new SortClassCustom.PostProfileByDateCreate());
            return new Profile(userAccountSetting,postDetails);
        }else {
            return null;
        }
    }

}
