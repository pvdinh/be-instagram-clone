package com.example.demo.services;

import com.example.demo.models.Like;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import com.example.demo.utils.SortClassCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;

    public List<String> getListUserLikedPost(String pId){
        List<String> listDisplayNameLikes = new ArrayList<>();
        List<Like> likes = likeRepository.findLikeByIdPost(pId);
        likes.sort(new SortClassCustom.LikeByDateLiked());
        likes.forEach(like -> {
            listDisplayNameLikes.add(userAccountSettingRepository.findUserAccountSettingById(like.getIdUser()).getUsername());
        });
        return listDisplayNameLikes;
    }
}
