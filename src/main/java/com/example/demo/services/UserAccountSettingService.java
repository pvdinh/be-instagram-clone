package com.example.demo.services;

import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.models.adminModels.UserAccountProfile;
import com.example.demo.models.comment.LikeComment;
import com.example.demo.models.feedback.Feedback;
import com.example.demo.models.group.Group;
import com.example.demo.models.profile.PostDetail;
import com.example.demo.models.profile.Profile;
import com.example.demo.repository.*;
import com.example.demo.utils.ConvertSHA1;
import com.example.demo.utils.SortClassCustom;
import com.example.demo.utils.UsernameFromJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
    private FollowRepository followRepository;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private HistorySearchUserService historySearchUserService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupMemberService groupMemberService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private SavedPostRepository savedPostRepository;
    @Autowired
    private LikeCommentRepository likeCommentRepository;
    @Autowired
    private ReplyCommentRepository replyCommentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountSettingService.class);

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
            //Khi tài khoản đã tồn tại , tiến hành update lại các thông tin ngoại trừ thông tin về posts, following, follower
            UserAccountSetting uAs = userAccountSettingRepository.findUserAccountSettingById(userAccountSetting.getId());
            userAccountSetting.setPosts(uAs.getPosts());
            userAccountSetting.setFollowing(uAs.getFollowing());
            userAccountSetting.setFollowers(uAs.getFollowers());
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
                if (post.getUserId().equals(userAccountService.getUID())) {
                    postDetails.add(new PostDetail(post, numberOfComments, listLikeString));
                } else if (post.getPrivacy() == 0) {
                    postDetails.add(new PostDetail(post, numberOfComments, listLikeString));
                }
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
            if (userAccount.getUsername().equals(username)) {
                userAccount.setUsername(username);
                userAccountRepository.save(userAccount);
                return PROFILE_SAVED;
            } else {
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
            if (oldPassword.equals(userAccount.getPassword())) {
                userAccount.setPassword(newPassword);
                userAccountRepository.save(userAccount);
                return CHANGED_PASSWORD;
            } else {
                return PASSWORD_INCORRECT;
            }
        } catch (Exception e) {
            return FAIL;
        }
    }

    public String changeProfilePhoto(HashMap<String, String> data) {
        String profilePhoto = data.get("profilePhoto");
        try {
            UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(userAccountService.getUID());
            if (userAccountSetting != null) {
                userAccountSetting.setProfilePhoto(profilePhoto);
                userAccountSettingRepository.save(userAccountSetting);
                return SUCCESS;
            } else {
                return FAIL;
            }
        } catch (Exception e) {
            return FAIL;
        }
    }

    public List<UserAccount> filterByTime(long start, long end, String email, String phone) {
        List<UserAccount> userAccountSettings = new ArrayList<>();
        try {
            if (start == 0 || end == 0) {
                userAccountSettings = userAccountRepository.filterByTimePhoneEmail(new Long(0), System.currentTimeMillis(), phone, email);
            } else {
                userAccountSettings = userAccountRepository.filterByTimePhoneEmail(start, end, phone, email);
            }
            return userAccountSettings;
        } catch (Exception e) {
            return userAccountSettings;
        }
    }


    public List<UserAccountProfile> filterByTimePageable(long start, long end, int page, int size, String email, String phone) {
        List<UserAccountProfile> userAccountProfiles = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
            if (start == 0 || end == 0) {
                userAccountRepository.filterByTimePhoneEmail(new Long(0), System.currentTimeMillis(), phone, email, pageable).forEach(userAccount -> {
                    userAccountProfiles.add(new UserAccountProfile(userAccount, userAccountSettingRepository.findUserAccountSettingById(userAccount.getId())));
                });
            } else {
                userAccountRepository.filterByTimePhoneEmail(start, end, phone, email, pageable).forEach(userAccount -> {
                    userAccountProfiles.add(new UserAccountProfile(userAccount, userAccountSettingRepository.findUserAccountSettingById(userAccount.getId())));
                });
            }
            return userAccountProfiles;
        } catch (Exception e) {
            return userAccountProfiles;
        }
    }


    public String adminDeleteUser(String idUser) {
        try {
            UserAccount userAccount = userAccountService.findUserAccountById(idUser);
            if (userAccount.getRoles().contains("ROLE_USER")) {
                //cap nhat lai so luong following cua nguoi khac
                followService.findFollowByUserFollowing(idUser).forEach(following -> {
                    UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(following.getUserCurrent());
                    if (userAccountSetting != null) {
                        userAccountSetting.setFollowing(userAccountSetting.getFollowing() - 1);
                        userAccountSettingRepository.save(userAccountSetting);
                    }
                });

                //cap nhat lai so luong follower cuar nguoi khac
                followService.findFollowByUserCurrent(idUser).forEach(follower -> {
                    UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(follower.getUserFollowing());
                    if (userAccountSetting != null) {
                        userAccountSetting.setFollowers(userAccountSetting.getFollowers() - 1);
                        userAccountSettingRepository.save(userAccountSetting);
                    }
                });


                //
                followRepository.deleteByUserCurrent(idUser);
                followRepository.deleteByUserFollowing(idUser);


                //xoa feedback
                feedbackService.deleteByIdUser(idUser);

                //xoa historySearchUser
                historySearchUserService.deleteByIdUser(idUser);

                //delete userAccount
                userAccountService.delete(idUser);

                //delete message
                messageService.deleteBySender(idUser);
                messageService.deleteByReceiver(idUser);

                //xoa thong tin nhom va nhom quan ly
                groupService.findByRoleAndIdUser("ADMIN", idUser).forEach(group -> {
                    groupMemberService.deleteByIdGroup(group.getId());
                    groupService.deleteById(group.getId());
                });
                groupService.findByRoleAndIdUser("MEMBER", idUser).forEach(group -> {
                    groupService.rejectRequestJoinGroup(group.getId(), idUser);
                });

                //delete activity
                activityService.deleteByIdCurrentUser(idUser);
                activityService.deleteByIdInteractUser(idUser);

                //delete story
                storyRepository.deleteByIdUser(idUser);

                //delete report
                reportRepository.deleteByIdUser(idUser);

                //delete like
                List<Like> likes = likeService.getByIdUser(idUser);
                likes.forEach(lk -> {
                    Post post = postService.findPostByLikes(lk.getId());
                    if (post != null) {
                        Like like = likeService.findLikeByIdUserAndIdPost(idUser, post.getId());
                        if (like != null) {
                            post.getLikes().remove(like.getId());
                            postRepository.save(post);
                        }
                    }
                });
                likeService.deleteByIdUSer(idUser);

                //delete saved post
                savedPostRepository.deleteByUserId(idUser);

                //delete like comment
                likeCommentRepository.deleteByIdUser(idUser);

                //delete reply comment
                replyCommentRepository.deleteByIdUser(idUser);

                //delete all post
                List<Post> posts = postRepository.findPostByUserId(idUser);
                postRepository.findPostByUserId(idUser).forEach(post -> {
                    postService.deletePost(post.getId());
                });

                userAccountSettingRepository.deleteById(idUser);


                LOGGER.info("delete success user id: {} .", idUser);
                return SUCCESS;
            }
            return FAIL;
        } catch (Exception e) {
            LOGGER.info("delete fails.");
            return FAIL;
        }
    }

}
