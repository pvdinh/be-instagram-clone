package com.example.demo.repository;

import com.example.demo.models.AuthProvider;
import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface UserAccountRepository extends MongoRepository<UserAccount,String> {
    UserAccount findUserAccountByUsernameOrEmailOrPhoneNumberOrId(String username,String email,String phoneNumber,String Id);
    UserAccount findUserAccountByUsername(String username);
    UserAccount findUserAccountById(String id);
    UserAccount findUserAccountByPhoneNumber(String phoneNumber);
    UserAccount findUserAccountByEmail(String email);
    List<UserAccount> findByIdOrUsernameContainsOrDisplayNameContains(String id, String username, String displayName, Pageable pageable);
    List<UserAccount> findByIdOrUsernameContainsOrDisplayNameContains(String id, String username, String displayName);

    List<UserAccount> findByAuthProvider(AuthProvider authProvider);


    @Query(value = "{'phoneNumber' : {$regex : '?2' }, 'email' : {$regex : '?3' }, 'dateCreated': {$gte: ?0, $lte:?1 }}")
    List<UserAccount> filterByTimePhoneEmail(Long start, Long end, String phone, String email);

    @Query(value = "{'phoneNumber' : {$regex : '?2' }, 'email' : {$regex : '?3' },'dateCreated': {$gte: ?0, $lte:?1 }}")
    List<UserAccount> filterByTimePhoneEmail(Long start, Long end, String phone, String email, Pageable pageable);

}
