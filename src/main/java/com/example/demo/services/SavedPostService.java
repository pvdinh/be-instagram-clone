package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.models.profile.PostDetail;
import com.example.demo.models.saved_post.SavedPost;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.SavedPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SavedPostService {
    @Autowired
    private SavedPostRepository savedPostRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public String beginSavePost(String postId){
        try{
            savedPostRepository.save(new SavedPost(userAccountService.getUID(),postId,System.currentTimeMillis()));
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public String endSavePost(String postId){
        try{
            SavedPost savedPost = savedPostRepository.findSavedPostByUserIdAndPostId(userAccountService.getUID(),postId);
            if (savedPost != null){
                savedPostRepository.delete(savedPost);
                return SUCCESS;
            }else return FAIL;
        }catch (Exception e){
            return FAIL;
        }
    }

    public boolean checkSavedPost(String postId){
        try{
            Post post = postRepository.findPostById(postId);
            if (post != null){
                SavedPost savedPosts = savedPostRepository.findSavedPostByUserIdAndPostId(userAccountService.getUID(),post.getId());
                if(savedPosts != null){
                    return true;
                }
                else return false;
            }else return false;
        }catch (Exception e){
            return false;
        }
    }

    public List<PostDetail> getListPostSavedFromUID(){
        List<PostDetail> postDetails = new ArrayList<>();
        List<SavedPost> savedPosts = savedPostRepository.findSavedPostByUserId(userAccountService.getUID());
        savedPosts.forEach(savedPost -> {
            Post post = postRepository.findPostById(savedPost.getPostId());
            List<String> stringListLikes = likeService.getListUserLikedPost(savedPost.getPostId());
            int numberOfComments = commentService.findCommentByIdPost(savedPost.getPostId()).size();
            postDetails.add(new PostDetail(post,numberOfComments,stringListLikes));
        });
        return postDetails;
    }

}
