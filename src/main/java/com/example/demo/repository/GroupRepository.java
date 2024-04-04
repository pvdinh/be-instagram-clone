package com.example.demo.repository;

import com.example.demo.models.group.Group;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group,String> {
    Group findByName(String name);
    List<Group> findByNameContains(String name, Pageable pageable);
}
