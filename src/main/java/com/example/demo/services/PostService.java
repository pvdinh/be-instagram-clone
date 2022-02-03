package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.models.activity.Activity;
import com.example.demo.models.profile.PostDetail;
import com.example.demo.repository.*;
import com.example.demo.utils.SortClassCustom;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    @Autowired
    private ActivityService activityService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findAllByUserId(String userId) {
        return postRepository.findPostByUserId(userId);
    }

    public Post findByPostId(String pId) {
        return postRepository.findPostById(pId);
    }

    public List<Post> findContainsByUserIdOrIdPageable(String search,int page,int size){
        List<Post> posts = findAll();
        try {
            Pageable pageable = PageRequest.of(page,size, Sort.by("dateCreated").descending());
            posts = postRepository.findByUserIdContainsOrId(search,search,pageable);
            return posts;
        }catch (Exception e){
            return posts;
        }
    }

    public List<Post> findContainsByUserIdOrId(String search){
        List<Post> posts = new ArrayList<>();
        try {
            posts = postRepository.findByUserIdContainsOrId(search,search);
            return posts;
        }catch (Exception e){
            return posts;
        }
    }

    public List<Post> findPostFilter(int typeFilter, int page, int size) {
        List<Post> posts = new ArrayList<>();
        try {
            switch (typeFilter) {
                case 1:
                    Pageable pageableASC = PageRequest.of(page, size, Sort.by("dateCreated").ascending());
                    posts = postRepository.findAll(pageableASC).getContent();
                    break;
                case 2:
                    Pageable pageableDES = PageRequest.of(page, size, Sort.by("dateCreated").descending());
                    posts = postRepository.findAll(pageableDES).getContent();
                    break;
                case 3: {
                    List<Post> finalPosts = posts;
                    getPostDetailPopular(page, size).forEach(postDetail -> {
                        finalPosts.add(postDetail.getPost());
                    });
                    return finalPosts;
                }
                default:
                    break;
            }
            return posts;
        } catch (Exception e) {
            return posts;
        }
    }

    public List<PostDetail> getPostDetailPopular(int page, int size) {
        List<PostDetail> postDetails = new ArrayList<>();
        try {
            List<Post> posts = postRepository.findAll();
            posts.forEach(post -> {
                List<Comment> comments = commentRepository.findCommentByIdPost(post.getId());
                postDetails.add(new PostDetail(post, comments.size(), Collections.emptyList()));
            });
            postDetails.sort(new SortClassCustom.PostByPopular());
            if (postDetails.size() < size && page < 1) {
                return postDetails;
            } else if (postDetails.size() < size && page >= 1) {
                return Collections.emptyList();
            }else if (postDetails.size() < ((page * size) + size)) {
                return postDetails.subList(page * size, postDetails.size());
            } else return postDetails.subList(page * size, (page * size) + size);
        } catch (Exception e) {
            return postDetails;
        }
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

    public List<PostInformation> getAllPostInformationFollowing(int page, int size) {
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
        if (postInformations.size() < size && page < 1) {
            return postInformations.subList(0, postInformations.size());
        } else if (postInformations.size() < size && page >= 1) {
            return Collections.emptyList();
        } else return postInformations.subList(page * size, (page * size) + size);
    }

    public String like(String uId, String pId) {
        Like like = new Like(pId, uId, System.currentTimeMillis());
        Like likeD = likeRepository.findLikeByIdUserAndIdPost(uId, pId);
        if (likeD == null) {
            likeRepository.insert(like);
            Post post = postRepository.findPostById(pId);
            post.getLikes().add(likeRepository.findLikeByIdUserAndIdPost(uId, pId).getId());
            postRepository.save(post);
            //Thêm vào activity
            if (!uId.equals(post.getUserId())) {
                activityService.insert(new Activity(userAccountService.getUID(), post.getUserId(), post.getId(), "like", 0, System.currentTimeMillis()));
            }
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
            //xoá khỏi activity
            if (uId != post.getUserId()) {
                Activity activity = activityService.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(userAccountService.getUID(), post.getUserId(), "like", post.getId());
                activityService.delete(activity);
            }
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public String postNewPost(Post post) {
        try {
            post.setDateCreated(System.currentTimeMillis());
            post.setLikes(Collections.emptyList());
            post.setUserId(userAccountService.getUID());
            if (post.getUserId() == "" || post.getUserId() == null) {
                return FAIL;
            } else {
                postRepository.insert(post);
                updatePostQuantity();
                return SUCCESS;
            }
        } catch (Exception e) {
            System.out.println("POST EXISTS");
            return FAIL;
        }
    }

    public String deletePost(String pId) {
        try {
            Post post = postRepository.findPostById(pId);
            String x = userAccountService.getUID();
            if (post != null && post.getUserId().equals(userAccountService.getUID())) {
                postRepository.delete(post);
                updatePostQuantity();
                return SUCCESS;
            } else return FAIL;
        } catch (Exception e) {
            return FAIL;
        }
    }

    public void updatePostQuantity() {
        //Cập nhật lại số lượng bài đăng khi thêm hoặc xoá
        UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(userAccountService.getUID());
        userAccountSetting.setPosts(postRepository.findPostByUserId(userAccountService.getUID()).size());
        userAccountSettingRepository.save(userAccountSetting);
    }

    public List<PostDetail> getPostVideo(String username, int page, int size) {
        List<PostDetail> postDetails = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
            UserAccount userAccount = userAccountService.findUserAccountByUsername(username);
            if (userAccount != null) {
                List<Post> posts = postRepository.findPostVideoByTypeAndUserId("video", userAccount.getId(), pageable);
                posts.forEach(post -> {
                    List<String> stringListLike = likeService.getListUserLikedPost(post.getId());
                    int numberOfComments = commentService.findCommentByIdPost(post.getId()).size();
                    postDetails.add(new PostDetail(post, numberOfComments, stringListLike));
                });
                return postDetails;
            } else return postDetails;
        } catch (Exception e) {
            return postDetails;
        }
    }
}
