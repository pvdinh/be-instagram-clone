package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.models.profile.PostDetail;
import com.example.demo.models.profile.Profile;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import com.example.demo.utils.ConvertSHA1;
import com.example.demo.utils.SortClassCustom;
import com.example.demo.utils.UsernameFromJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserAccountSettingService {
    private final String SUCCESS = "success";
    private final String FAIL = "fail";
    private final String EXISTS = "Another account is using ";
    private final String NOT_AVAILABLE_USERNAME = "This username isn't available. Please try another.";
    private final String NOT_AVAILABLE_NAME = "This display name isn't available. Please try another.";
    private final String PROFILE_SAVED = "Profile saved.";
    private final String CHANGED_USERNAME = "Username changed. Please login again";
    private final String CHANGED_PASSWORD = "Password changed.";
    private final String PASSWORD_INCORRECT = "Your old password was entered incorrectly. Please enter it again.";
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private FollowService followService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    public UserAccountSetting findUserAccountSettingByUsername(String username) {
        return userAccountSettingRepository.findUserAccountSettingByUsername(username);
    }

    public UserAccountSetting findUserAccountSettingById(String id) {
        return userAccountSettingRepository.findUserAccountSettingById(id);
    }

    public List<UserAccountSetting> findUserAccountSettingsByUsernameContains(String search) {
        return userAccountSettingRepository.findUserAccountSettingsByUsernameContains(search);
    }

    public UserAccountSetting findUserAccountSettingByJwt() {
        UserAccount userAccount = userAccountService.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(UsernameFromJWT.get());
        return userAccountSettingRepository.findUserAccountSettingByUsername(userAccount.getUsername());
    }

    public String addUserAccountSetting(UserAccountSetting userAccountSetting) {
        try {
            userAccountSettingRepository.insert(userAccountSetting);
            followService.insert(userAccountSetting.getId());
            return SUCCESS;
        } catch (Exception e) {
            userAccountSettingRepository.save(userAccountSetting);
            System.out.println("Update Account EXISTS");
            return FAIL;
        }
    }

    public Profile findUserProfileByUsername(String username) {
        UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingByUsername(username);
        if (userAccountSetting != null) {
            List<PostDetail> postDetails = new ArrayList<>();
            List<Post> posts = postService.findAllByUserId(userAccountSetting.getId());
            posts.forEach(post -> {
                List<String> listLikeString = likeService.getListUserLikedPost(post.getId());
                int numberOfComments = commentService.findCommentByIdPost(post.getId()).size();
                postDetails.add(new PostDetail(post, numberOfComments, listLikeString));
            });
            postDetails.sort(new SortClassCustom.PostProfileByDateCreate());
            return new Profile(userAccountSetting, postDetails);
        } else {
            return null;
        }
    }

    public String editUserAccountSetting(HashMap<String, String> user) {
        String displayName = user.get("displayName");
        String username = user.get("username");
        String website = user.get("website");
        String bio = user.get("bio");
        String email = user.get("email");
        String phoneNumber = user.get("phoneNumber");
        UserAccount userAccount = new UserAccount();
        UserAccountSetting userAccountSetting = new UserAccountSetting();
        try {
            try {
                //update in table userAccountSetting
                userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(userAccountService.getUID());
                userAccountSetting.setDisplayName(displayName);
                userAccountSetting.setUsername(username);
                userAccountSetting.setWebsite(website);
                userAccountSetting.setDescription(bio);
                userAccountSettingRepository.save(userAccountSetting);
            } catch (Exception e) {
                String err = e.getMessage();
                if (err.contains("displayName dup key")) {
                    return NOT_AVAILABLE_NAME;
                } else if (err.contains("username dup key")) {
                    return NOT_AVAILABLE_USERNAME;
                } else
                    return FAIL;
            }
            //update in table userAccount
            userAccount = userAccountService.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(userAccountService.getUID());
            userAccount.setEmail(email);
            userAccount.setPhoneNumber(phoneNumber);
            if(userAccount.getUsername().equals(username)){
                userAccount.setUsername(username);
                userAccountRepository.save(userAccount);
                return PROFILE_SAVED;
            }else {
                userAccount.setUsername(username);
                userAccountRepository.save(userAccount);
                return CHANGED_USERNAME;
            }

        } catch (Exception e) {
            String err = e.getMessage();
            if (err.contains("email dup key")) {
                return EXISTS + email;
            } else if (err.contains("phoneNumber dup key")) {
                return EXISTS + phoneNumber;
            } else if (err.contains("username dup key")) {
                return NOT_AVAILABLE_USERNAME;
            } else
                return FAIL;
        }
    }

    public HashMap<String, String> getPrivateInfo() {
        HashMap<String, String> privateInfo = new HashMap<>();
        try {
            UserAccount userAccount = userAccountService.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(userAccountService.getUID());
            privateInfo.put("email", userAccount.getEmail());
            privateInfo.put("phoneNumber", userAccount.getPhoneNumber());
            return privateInfo;
        } catch (Exception e) {
            return privateInfo;
        }
    }

    public String changePassword(HashMap<String, String> data) {
        String oldPassword = ConvertSHA1.convertSHA1(data.get("oldPassword"));
        String newPassword = ConvertSHA1.convertSHA1(data.get("newPassword"));
        try {
            UserAccount userAccount = userAccountService.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(userAccountService.getUID());
            if(oldPassword.equals(userAccount.getPassword())){
                userAccount.setPassword(newPassword);
                userAccountRepository.save(userAccount);
                return CHANGED_PASSWORD;
            }else {
                return PASSWORD_INCORRECT;
            }
        } catch (Exception e) {
            return FAIL;
        }
    }

}
