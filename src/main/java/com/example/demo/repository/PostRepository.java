package com.example.demo.repository;

import com.example.demo.models.Post;
import javafx.geometry.Pos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends MongoRepository<Post,String> {
    Post findPostById(String id);
    List<Post> findPostByUserId(String userId);

    List<Post> findPostVideoByTypeAndUserId(String type, String uId, Pageable pageable);

    List<Post> findByUserIdContainsOrId(String uId,String pId,Pageable pageable);
    List<Post> findByUserIdContainsOrId(String uId,String pId);

    @Query("[{$addFields: {\n" +
            " year: {\n" +
            "  $year: {\n" +
            "   $toDate: '$dateCreated'\n" +
            "  }\n" +
            " }\n" +
            "}}, {$match: {\n" +
            " year: 2022\n" +
            "}}, {$group: {\n" +
            " _id: {\n" +
            "  $month: {\n" +
            "   $toDate: '$dateCreated'\n" +
            "  }\n" +
            " },\n" +
            " count: {\n" +
            "  $sum: 1\n" +
            " }\n" +
            "}}]")
    Object postData();
}
