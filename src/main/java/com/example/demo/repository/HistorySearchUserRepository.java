package com.example.demo.repository;

import com.example.demo.models.SearchInLayout.HistorySearchUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorySearchUserRepository extends MongoRepository<HistorySearchUser,String> {
    List<HistorySearchUser> findHistorySearchUserByIdUser(String idUser);
}
