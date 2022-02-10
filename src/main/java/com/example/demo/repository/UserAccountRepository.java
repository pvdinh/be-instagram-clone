package com.example.demo.repository;

import com.example.demo.models.UserAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.stream.Stream;

public interface UserAccountRepository extends MongoRepository<UserAccount,String> {
    UserAccount findUserAccountByUsernameOrEmailOrPhoneNumberOrId(String username,String email,String phoneNumber,String Id);
    UserAccount findUserAccountByUsername(String username);
    UserAccount findUserAccountById(String id);
    UserAccount findUserAccountByPhoneNumber(String phoneNumber);
    UserAccount findUserAccountByEmail(String email);
    List<UserAccount> findByIdContainsOrUsernameContainsOrDisplayNameContains(String id, String username, String displayName, Pageable pageable);
    List<UserAccount> findByIdContainsOrUsernameContainsOrDisplayNameContains(String id, String username, String displayName);
}
