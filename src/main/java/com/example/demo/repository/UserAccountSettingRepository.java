package com.example.demo.repository;

import com.example.demo.models.UserAccountSetting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountSettingRepository extends MongoRepository<UserAccountSetting,String> {
    UserAccountSetting findUserAccountSettingByUsername(String username);
}
