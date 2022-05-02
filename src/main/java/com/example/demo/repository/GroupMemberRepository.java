package com.example.demo.repository;

import com.example.demo.models.group.GroupMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends MongoRepository<GroupMember, String> {
    List<GroupMember> findByRoleAndIdUser(String role, String idUSer, Pageable pageable);
    List<GroupMember> findByIdUserAndStatus(String role,int status);
    List<GroupMember> findByIdGroup(String idGroup);
    GroupMember findByIdGroupAndIdUser(String idGroup,String idUser);
}
