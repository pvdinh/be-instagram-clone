package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import com.example.demo.utils.SortClassCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findAllByUserId(String userId) {
        return postRepository.findPostByUserId(userId);
    }

    public List<Post> findAllByListUserId(List<String> listUserId) {
        List<Post> posts = new ArrayList<>();
        listUserId.forEach(s -> {
            posts.addAll(postRepository.findPostByUserId(s));
        });
        return posts;
    }

    public PostInformation getPostInformationOfUser(String pId) {
        Post post = postRepository.findPostById(pId);
        if (post != null) {
            UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(post.getUserId());
            return new PostInformation(post, userAccountSetting, likeService.getListUserLikedPost(post.getId()));
        } else {
            return new PostInformation(null, null, null);
        }
    }

    public List<PostInformation> getAllPostInformationFollowing() {
        List<PostInformation> postInformations = new ArrayList<>();
        List<UserAccountSetting> userAccountSettings = new ArrayList<>();
        List<Follow> follows = followService.findFollowByUserCurrent();
        follows.forEach(follow -> {
            userAccountSettings.add(userAccountSettingRepository.findUserAccountSettingById(follow.getUserFollowing()));
        });
        userAccountSettings.forEach(userAccountSetting -> {
            List<Post> posts = postRepository.findPostByUserId(userAccountSetting.getId());
            posts.forEach(post -> {
                postInformations.add(new PostInformation(post, userAccountSetting, likeService.getListUserLikedPost(post.getId())));
            });
        });
        postInformations.sort(new SortClassCustom.PostByDateCreate());
        return postInformations;
    }

    public String like(String uId, String pId) {
        Like like = new Like(pId, uId, System.currentTimeMillis());
        Like likeD = likeRepository.findLikeByIdUserAndIdPost(uId, pId);
        if (likeD == null) {
            likeRepository.insert(like);
            Post post = postRepository.findPostById(pId);
            post.getLikes().add(likeRepository.findLikeByIdUserAndIdPost(uId, pId).getId());
            postRepository.save(post);
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public String unLike(String uId, String pId) {
        Like like = likeRepository.findLikeByIdUserAndIdPost(uId, pId);
        if (like != null) {
            Post post = postRepository.findPostById(pId);
            post.getLikes().remove(likeRepository.findLikeByIdUserAndIdPost(uId, pId).getId());
            postRepository.save(post);
            likeRepository.delete(like);
            return SUCCESS;
        } else {
            return FAIL;
        }
    }
}
