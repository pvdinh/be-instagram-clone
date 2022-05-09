package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.models.PostInformation;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.models.group.Group;
import com.example.demo.models.group.GroupInformation;
import com.example.demo.models.group.GroupMember;
import com.example.demo.models.group.MemberInGroup;
import com.example.demo.repository.GroupMemberRepository;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import com.example.demo.utils.SortClassCustom;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Sorts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GroupService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    private final String SUCCESS = "success";
    private final String FAIL = "fail";
    private final String DUPLICATE = "duplicate";

    public GroupInformation findByIdGroupAndIdUser(String idGroup) {
        GroupInformation groupInformation = new GroupInformation();
        try {
            groupInformation.setGroup(groupRepository.findById(idGroup).orElse(null));
            groupInformation.setGroupMember(groupMemberRepository.findByIdGroupAndIdUser(idGroup, userAccountService.getUID()));
            LOGGER.info("get success.");
            return groupInformation;
        } catch (Exception e) {
            LOGGER.info("get fail cause:", e.getCause());
            return groupInformation;
        }
    }

    public Group findById(String idGroup) {
        return groupRepository.findById(idGroup).orElse(null);
    }

    public List<Group> findByRole(String role, int page, int size) {
        List<Group> groups = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
            List<GroupMember> groupMembers = groupMemberRepository.findByRoleAndIdUserAndStatus(role, userAccountService.getUID(),1, pageable);
            groupMembers.forEach(groupMember -> {
                groups.add(groupRepository.findById(groupMember.getIdGroup()).orElse(new Group()));
            });
            LOGGER.info("get success.");
            return groups;
        } catch (Exception e) {
            LOGGER.info("get fail cause:", e.getCause());
            return groups;
        }
    }

    public List<Group> findByRoleAndIdUser(String role,String idUser) {
        List<Group> groups = new ArrayList<>();
        try {
            List<GroupMember> groupMembers = groupMemberRepository.findByRoleAndIdUser(role, idUser);
            groupMembers.forEach(groupMember -> {
                groups.add(groupRepository.findById(groupMember.getIdGroup()).orElse(new Group()));
            });
            LOGGER.info("get success.");
            return groups;
        } catch (Exception e) {
            LOGGER.info("get fail cause:", e.getCause());
            return groups;
        }
    }

    public List<PostInformation> getAllPostInGroupSelf(int page, int size) {
        List<PostInformation> postInformations = new ArrayList<>();
        try {
            //lay ra cac nhom da tham gia
            List<GroupMember> groupMembers = groupMemberRepository.findByIdUserAndStatus(userAccountService.getUID(), 1);
            groupMembers.forEach(groupMember -> {
                //lay bai viet tuong ung voi tung nhom.
                List<Post> posts = postRepository.findPostByIdGroup(groupMember.getIdGroup());
                posts.forEach(post -> {
                    postInformations.add(new PostInformation(post,
                            userAccountSettingRepository.findUserAccountSettingById(groupMember.getIdUser()),
                            likeService.getListUserLikedPost(post.getId()), groupRepository.findById(post.getIdGroup()).orElse(null)));
                });
            });
            postInformations.sort(new SortClassCustom.PostByDateCreate());
            LOGGER.info("get success.");
            if (postInformations.size() < size && page < 1) {
                return postInformations.subList(0, postInformations.size());
            } else if (postInformations.size() < size && page >= 1) {
                return Collections.emptyList();
            } else return postInformations.subList(page * size, (page * size) + size);
        } catch (Exception e) {
            LOGGER.info("get fail cause:", e.getCause());
            return postInformations;
        }
    }

    public List<PostInformation> getAllPostInGroup(String idGroup, int page, int size) {
        List<PostInformation> postInformations = new ArrayList<>();
        try {
            //lay ra cac bai dang tu 1 nhom
            Pageable pageable = PageRequest.of(page,size, Sort.by("dateCreated").descending());
            List<Post> posts = postRepository.findPostByIdGroup(idGroup,pageable);
            posts.forEach(post -> {
                postInformations.add(new PostInformation(post,
                        userAccountSettingRepository.findUserAccountSettingById(post.getUserId()),
                        likeService.getListUserLikedPost(post.getId()), groupRepository.findById(post.getIdGroup()).orElse(null)));
            });
            LOGGER.info("get success.");
            return postInformations;
        } catch (Exception e) {
            LOGGER.info("get fail cause:", e.getCause());
            return postInformations;
        }
    }

    public List<Group> findByNameContain(String name) {
        List<Group> groups = new ArrayList<>();
        try
        {
            Pageable pageable = PageRequest.of(0, 20);
            groups = groupRepository.findByNameContains(name, pageable);
            LOGGER.info("get success.");
            return groups;
        }catch (Exception e){
            LOGGER.info("get fail cause:", e.getCause());
            return groups;
        }
    }

    public List<MemberInGroup> getMemberInGroup(String idGroup,int status,int page,int size) {
        List<MemberInGroup> memberInGroups = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page,size,Sort.by("dateCreated").descending());
            groupMemberRepository.findByIdGroupAndStatus(idGroup,status,pageable).forEach(groupMember -> {
                UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(groupMember.getIdUser());
                UserAccountSetting userInvite = userAccountSettingRepository.findUserAccountSettingById(groupMember.getIdUserInvite());
                memberInGroups.add(new MemberInGroup(groupMember,userAccountSetting,userInvite));
            });
            LOGGER.info("get success member: {}", memberInGroups.size());
            return memberInGroups;
        }catch (Exception e){
            LOGGER.error("get member fail: {}", e.getCause());
            return memberInGroups;
        }
    }

    public List<MemberInGroup> searchMemberInGroup(String search,String idGroup,int status,int page,int size) {
        List<MemberInGroup> memberInGroups = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page,size,Sort.by("dateCreated").descending());
            List<UserAccountSetting> userAccountSettings = userAccountSettingRepository.findUserAccountSettingsByUsernameContains(search);
            userAccountSettings.forEach(userAccountSetting -> {
                GroupMember groupMember = groupMemberRepository.findByIdGroupAndIdUserAndStatus(idGroup,userAccountSetting.getId(),status);
                if(groupMember != null){
                    UserAccountSetting userInvite = userAccountSettingRepository.findUserAccountSettingById(groupMember.getIdUserInvite());
                    memberInGroups.add(new MemberInGroup(groupMember,userAccountSetting,userInvite));
                }
            });
            LOGGER.info("search success member: {}", memberInGroups.size());
            return memberInGroups;
        }catch (Exception e){
            LOGGER.error("search member fail: {}", e.getCause());
            return memberInGroups;
        }
    }

    public String addMemberIntoGroup(GroupMember gm) {
        try {
            GroupMember gmc = groupMemberRepository.findByIdGroupAndIdUser(gm.getIdGroup(), gm.getIdUser());
            if (gmc == null) {
                //
                GroupMember groupMember = new GroupMember();
                groupMember.setDateCreated(System.currentTimeMillis());
                groupMember.setDateJoined(System.currentTimeMillis());
                groupMember.setIdGroup(gm.getIdGroup());
                groupMember.setIdUser(gm.getIdUser());
                groupMember.setRole("MEMBER");
                groupMember.setStatus(1);
                groupMember.setIdUserInvite(userAccountService.getUID());
                groupMemberRepository.insert(groupMember);


                //update number membership
                Group group = groupRepository.findById(gm.getIdGroup()).orElse(null);
                group.setNumberMembership(groupMemberRepository.findByIdGroup(gm.getIdGroup()).size());
                groupRepository.save(group);

                LOGGER.info("insert success member: {}", groupMember.getId());
                return SUCCESS;
            } else return DUPLICATE;

        } catch (Exception e) {
            LOGGER.error("insert member fail: {}", e.getCause());
            return FAIL;
        }
    }
    public String requestJoinGroup(String idGroup) {
        try {
            GroupMember gmc = groupMemberRepository.findByIdGroupAndIdUser(idGroup, userAccountService.getUID());
            if (gmc == null) {
                //
                GroupMember groupMember = new GroupMember();
                groupMember.setDateCreated(System.currentTimeMillis());
                groupMember.setDateJoined(System.currentTimeMillis());
                groupMember.setIdGroup(idGroup);
                groupMember.setIdUser(userAccountService.getUID());
                groupMember.setRole("MEMBER");
                groupMember.setStatus(0);
                groupMember.setIdUserInvite(userAccountService.getUID());
                groupMemberRepository.insert(groupMember);
                LOGGER.info("request success member: {}", groupMember.getId());
                return SUCCESS;
            } else return DUPLICATE;

        } catch (Exception e) {
            LOGGER.error("request member fail: {}", e.getCause());
            return FAIL;
        }
    }

    public String confirmMemberRequest(GroupMember groupMember) {
        try {
            GroupMember gmc = groupMemberRepository.findByIdGroupAndIdUser(groupMember.getIdGroup(), groupMember.getIdUser());
            GroupMember checkRole = groupMemberRepository.findByIdGroupAndIdUser(groupMember.getIdGroup(), userAccountService.getUID());
            if (gmc != null && checkRole.getRole().equals("ADMIN") ) {

                //
                gmc.setStatus(1);
                gmc.setDateJoined(System.currentTimeMillis());
                groupMemberRepository.save(gmc);
                //

                //update number membership
                Group group = groupRepository.findById(groupMember.getIdGroup()).orElse(null);
                group.setNumberMembership(groupMemberRepository.findByIdGroup(groupMember.getIdGroup()).size());
                groupRepository.save(group);

                LOGGER.info("confirm success member: {}", gmc.getId());
                return SUCCESS;
            } else return FAIL;

        } catch (Exception e) {
            LOGGER.error("confirm member fail: {}", e.getCause());
            return FAIL;
        }
    }

    public String cancelMemberRequest(GroupMember groupMember) {
        try {
            GroupMember gmc = groupMemberRepository.findByIdGroupAndIdUser(groupMember.getIdGroup(), groupMember.getIdUser());
            GroupMember checkRole = groupMemberRepository.findByIdGroupAndIdUser(groupMember.getIdGroup(), userAccountService.getUID());
            if (gmc != null && checkRole.getRole().equals("ADMIN") ) {
                //
                gmc.setStatus(1);
                groupMemberRepository.delete(gmc);
                LOGGER.info("confirm success member: {}", gmc.getId());
                return SUCCESS;
            } else return FAIL;

        } catch (Exception e) {
            LOGGER.error("confirm member fail: {}", e.getCause());
            return FAIL;
        }
    }

    public String cancelRequestJoinGroup(String idGroup) {
        try {
            GroupMember gmc = groupMemberRepository.findByIdGroupAndIdUser(idGroup, userAccountService.getUID());
            if (gmc != null) {
                //
                groupMemberRepository.delete(gmc);
                LOGGER.info("cancel request success member: {}", gmc.getId());
                return SUCCESS;
            } else return FAIL;

        } catch (Exception e) {
            LOGGER.error("cancel request member fail: {}", e.getCause());
            return FAIL;
        }
    }

    public String rejectRequestJoinGroup(String idGroup,String idUser) {
        try {
            GroupMember gmc = groupMemberRepository.findByIdGroupAndIdUser(idGroup, idUser);
            if (gmc != null && !gmc.getRole().equals("ADMIN")) {
                //
                groupMemberRepository.delete(gmc);
                if(gmc.getStatus() == 1){
                    //update number membership when delete
                    Group group = groupRepository.findById(idGroup).orElse(null);
                    group.setNumberMembership(groupMemberRepository.findByIdGroup(idGroup).size());
                    groupRepository.save(group);
                }

                LOGGER.info("reject request success member: {}", gmc.getId());
                return SUCCESS;
            } else return FAIL;

        } catch (Exception e) {
            LOGGER.error("reject request member fail: {}", e.getCause());
            return FAIL;
        }
    }

    public String insert(Group group) {
        try {
            try {
                // tao group, nguoi tao se la admin
                Group g = groupRepository.findByName(group.getName());
                if (g == null) {
                    group.setDateCreated(System.currentTimeMillis());
                    group.setImageCover("https://www.facebook.com/images/groups/groups-default-cover-photo-2x.png");
                    group.setNumberMembership(1);
                    groupRepository.insert(group);

                    //
                    Group gr = groupRepository.findByName(group.getName());
                    GroupMember groupMember = new GroupMember();
                    groupMember.setDateCreated(System.currentTimeMillis());
                    groupMember.setDateJoined(0);
                    groupMember.setIdGroup(gr.getId());
                    groupMember.setIdUser(userAccountService.getUID());
                    groupMember.setRole("ADMIN");
                    groupMember.setStatus(1);
                    groupMember.setIdUserInvite("0");
                    groupMemberRepository.insert(groupMember);

                    LOGGER.info("insert success group: {}", group);

                    return SUCCESS;
                } else {

                    LOGGER.error("duplicate group name: {}", g.getName());

                    return DUPLICATE;
                }
            } catch (Exception e) {
                LOGGER.error("duplicate group name: {}", group.getName());

                return DUPLICATE;
            }
        } catch (Exception e) {
            LOGGER.error("insert fail: {}", group);

            return FAIL;
        }
    }

    void deleteById(String idGroup){
        groupRepository.deleteById(idGroup);
    }
}
