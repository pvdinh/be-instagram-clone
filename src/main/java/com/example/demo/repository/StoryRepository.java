package com.example.demo.repository;

import com.example.demo.models.home.Story;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StoryRepository extends MongoRepository<Story,String> {
    Story findStoryByIdUserAndIdPost(String idUser,String idPost);
    List<Story> findStoryByIdUser(String idUser);
    List<Story> findStoryByIdPost(String idPost);
    Story findStoryById(String idStory);
    void deleteByIdPost(String idPost);

}
