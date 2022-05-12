package com.example.demo.repository;

import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserAccountSettingRepository extends MongoRepository<UserAccountSetting, String> {
    UserAccountSetting findUserAccountSettingByUsername(String username);

    UserAccountSetting findUserAccountSettingById(String id);

    List<UserAccountSetting> findUserAccountSettingsByUsernameContains(String search);
}
