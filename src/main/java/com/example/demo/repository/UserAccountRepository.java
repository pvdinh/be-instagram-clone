package com.example.demo.repository;

import com.example.demo.models.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface UserAccountRepository extends MongoRepository<UserAccount,String> {
    UserAccount findUserAccountByUsernameOrEmailOrPhoneNumber(String username,String email,String phoneNumber);
}
