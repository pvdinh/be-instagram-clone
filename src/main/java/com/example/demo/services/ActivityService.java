package com.example.demo.services;

import com.example.demo.models.PostInformation;
import com.example.demo.models.activity.Activity;
import com.example.demo.models.activity.ActivityInformation;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.utils.AuthenticationCurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<ActivityInformation> findAll(int page,int size){
        List<Activity> activities = new ArrayList<>();
        List<ActivityInformation> activityInformations = new ArrayList<>();
        try{
            Pageable pageable = PageRequest.of(page,size, Sort.by("dateActivity").descending());
            activities=activityRepository.findByIdInteractUser(userAccountService.getUID(),pageable);
            activities.forEach(activity -> {
                activityInformations.add(new ActivityInformation(activity
                        ,userAccountSettingService.findUserAccountSettingById(activity.getIdCurrentUser())
                        ,postService.findByPostId(activity.getIdPost()),likeService.getListUserLikedPost(activity.getIdPost()),commentService.getListUserCommentedPost(activity.getIdPost())));
            });
            return activityInformations;
        }catch (Exception e){
            return activityInformations;
        }
    }

    public Activity findActivityByIdCurrentUserAndIdInteractUserAndTypeActivity(String idCurrentUser,String idInteractUser,String typeActivity){
        return activityRepository.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivity(idCurrentUser,idInteractUser,typeActivity);
    }

    public Activity findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(String idCurrentUser,String idInteractUser,String typeActivity,String idPost){
        return activityRepository.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(idCurrentUser,idInteractUser,typeActivity,idPost);
    }

    public String insert(Activity activity){
        try {
            Activity ac = new Activity();
            if(activity.getIdPost() != null){
                ac = findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(activity.getIdCurrentUser(),activity.getIdInteractUser(),activity.getTypeActivity(),activity.getIdPost());
            }else {
                ac = findActivityByIdCurrentUserAndIdInteractUserAndTypeActivity(activity.getIdCurrentUser(),activity.getIdInteractUser(),activity.getTypeActivity());
            }
            if(ac == null && authenticationCurrentUser.checkCurrentUser(activity.getIdCurrentUser())){
                activityRepository.insert(activity);
            }else return FAIL;
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public String delete(Activity activity){
        try {
            if(authenticationCurrentUser.checkCurrentUser(activity.getIdCurrentUser())){
                activityRepository.delete(activity);
            }else return FAIL;
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

}
