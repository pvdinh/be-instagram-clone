package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.models.comment.Comment;
import com.example.demo.models.profile.PostDetail;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExploreService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    public List<PostDetail> getExplorePosts(int page, int size){
        List<PostDetail> postDetails = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page,size);
            Page<Post> postPage = postRepository.findAll(pageable);
            postPage.getContent().forEach(post -> {
                if(post.getPrivacy() == 0){
                    List<Comment> comments = commentRepository.findCommentByIdPost(post.getId());
                    postDetails.add(new PostDetail(post,comments.size(), Collections.emptyList()));
                }
            });
            return postDetails;
        }catch (Exception e){
            return postDetails;
        }
    }
}
