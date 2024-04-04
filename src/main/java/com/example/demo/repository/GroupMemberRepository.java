package com.example.demo.repository;

import com.example.demo.models.group.GroupMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends MongoRepository<GroupMember, String> {
    List<GroupMember> findByRoleAndIdUserAndStatus(String role, String idUSer,Integer status ,Pageable pageable);
    List<GroupMember> findByRoleAndIdUserAndStatus(String role, String idUSer,Integer status);
    List<GroupMember> findByRoleAndIdUser(String role, String idUSer);
    List<GroupMember> findByIdUserAndStatus(String idUser,int status);
    List<GroupMember> findByIdGroupAndStatus(String idGroup,int status,Pageable pageable);
    List<GroupMember> findByIdGroup(String idGroup);
    GroupMember findByIdGroupAndIdUser(String idGroup,String idUser);
    GroupMember findByIdGroupAndIdUserAndStatus(String idGroup,String idUser,Integer status);
    void deleteByIdGroup(String idGroup);
}
