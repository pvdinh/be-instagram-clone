package com.example.demo.services;

import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.UserAccount;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserAccountRepository;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
    private UserAccountRepository userAccountRepository;

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
