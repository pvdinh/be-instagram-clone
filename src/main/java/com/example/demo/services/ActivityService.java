package com.example.demo.services;

import com.example.demo.models.activity.Activity;
import com.example.demo.models.activity.ActivityInformation;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.utils.AuthenticationCurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private AuthenticationCurrentUser authenticationCurrentUser;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private PostService postService;
    @Autowired
    private UserAccountSettingService userAccountSettingService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyCommentService replyCommentService;


    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<ActivityInformation> findAll(int page,int size){
        List<Activity> activities = new ArrayList<>();
        List<ActivityInformation> activityInformations = new ArrayList<>();
        try{
            Pageable pageable = PageRequest.of(page,size, Sort.by("dateActivity").descending());
            activities=activityRepository.findByIdInteractUser(userAccountService.getUID(),pageable);
            activities.forEach(activity -> {
                if(!checkActivityExists(activity,activityInformations)){
                    final String likeComment = "likeComment";
                    final String replyComment = "replyComment";
                    if(activity.getTypeActivity().equals(likeComment)){
                        activityInformations.add(new ActivityInformation(activity
                                ,userAccountSettingService.findUserAccountSettingById(activity.getIdCurrentUser())
                                ,postService.findByPostId(activity.getIdPost()), Collections.emptyList(),Collections.emptyList(),replyCommentService.getListUserLikeOrReplyComment(activity,likeComment),Collections.emptyList()));
                    }else if(activity.getTypeActivity().equals(replyComment)){
                        activityInformations.add(new ActivityInformation(activity
                                ,userAccountSettingService.findUserAccountSettingById(activity.getIdCurrentUser())
                                ,postService.findByPostId(activity.getIdPost()), Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),replyCommentService.getListUserLikeOrReplyComment(activity,replyComment)));
                    }
                    else {
                        activityInformations.add(new ActivityInformation(activity
                                ,userAccountSettingService.findUserAccountSettingById(activity.getIdCurrentUser())
                                ,postService.findByPostId(activity.getIdPost()),likeService.getListUserLikedPost(activity.getIdPost()),commentService.getListUserCommentedPost(activity.getIdPost())));
                    }
                }
            });
            return activityInformations;
        }catch (Exception e){
            return activityInformations;
        }
    }

    public boolean checkActivityExists(Activity activity,List<ActivityInformation> activityInformations){
        for (int i = 0; i < activityInformations.size(); i++) {
            if(activity.getIdPost() == null || activityInformations.get(i).getPost() == null) return false;
            if(activity.getIdPost().equals(activityInformations.get(i).getPost().getId()) && activity.getTypeActivity().equals(activityInformations.get(i).getActivity().getTypeActivity())){
                return true;
            }
        }
        return false;
    }

    public Activity findActivityByIdCurrentUserAndIdInteractUserAndTypeActivity(String idCurrentUser,String idInteractUser,String typeActivity){
        return activityRepository.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivity(idCurrentUser,idInteractUser,typeActivity);
    }

    public Activity findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(String idCurrentUser,String idInteractUser,String typeActivity,String idPost){
        return activityRepository.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(idCurrentUser,idInteractUser,typeActivity,idPost);
    }

    public String insert(Activity activity,String idCurrentUser){
        try {
            Activity ac = new Activity();
            if(activity.getIdPost() != null){
                ac = findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(activity.getIdCurrentUser(),activity.getIdInteractUser(),activity.getTypeActivity(),activity.getIdPost());
            }else {
                ac = findActivityByIdCurrentUserAndIdInteractUserAndTypeActivity(activity.getIdCurrentUser(),activity.getIdInteractUser(),activity.getTypeActivity());
            }
            if(ac == null){
                activityRepository.insert(activity);
            }else {
                ac.setDateActivity(System.currentTimeMillis());
                activityRepository.save(ac);
            };
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public String delete(Activity activity,String idCurrentUser){
        try {
            activityRepository.delete(activity);
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public String update(Activity activity){
        try {
            if(authenticationCurrentUser.checkCurrentUser(activity.getIdInteractUser())){
                activityRepository.save(activity);
                return SUCCESS;
            }else return FAIL;
        }catch (Exception e){
            return FAIL;
        }
    }

    public void deleteByIdCurrentUser(String id){
        activityRepository.deleteByIdCurrentUser(id);
    }

    public void deleteByIdInteractUser(String id){
        activityRepository.deleteByIdInteractUser(id);
    }


}
