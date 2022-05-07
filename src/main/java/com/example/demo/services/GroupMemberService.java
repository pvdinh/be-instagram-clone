package com.example.demo.services;

import com.example.demo.repository.GroupMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupMemberService {

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    void deleteByIdGroup(String idGroup){
        groupMemberRepository.deleteByIdGroup(idGroup);
    }

}
