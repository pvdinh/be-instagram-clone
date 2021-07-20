package com.example.demo.repository;

import com.example.demo.models.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface UserAccountRepository extends MongoRepository<UserAccount,String> {
    UserAccount findUserAccountByUsernameOrEmailOrPhoneNumberOrId(String username,String email,String phoneNumber,String Id);
    UserAccount findUserAccountByUsername(String username);
    UserAccount findUserAccountByPhoneNumber(String phoneNumber);
    UserAccount findUserAccountByEmail(String email);
}
