package com.example.demo.helper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class RedisHelper {

    @Autowired
    private RedisTemplate redisTemplate;

    public void saveObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteObject(String key) {
        redisTemplate.delete(key);
    }
}
