package com.example.demo.controllers.group;

import com.example.demo.models.Post;
import com.example.demo.models.group.Group;
import com.example.demo.models.group.GroupMember;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.example.demo.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupService groupService;

    //check role user in group to display front end
    @GetMapping("/{idGroup}")
    public BaseResponse findById(@PathVariable(name = "idGroup") String idGroup) {
        try {
            return new ResponseObject(HttpStatus.OK.value(), groupService.findByIdGroupAndIdUser(idGroup));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/{name}/search")
    public BaseResponse findByNameContain(@PathVariable(name = "name") String name) {
        try {
            return new ResponseData(HttpStatus.OK.value(), groupService.findByNameContain(name));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/{role}/group-role")
    public BaseResponse findByRole(@PathVariable(name = "role") String role, @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseData(HttpStatus.OK.value(), groupService.findByRole(role,currentPage,pageSize));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/posts")
    public BaseResponse getAllPostInAllGroupSelf(@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseData(HttpStatus.OK.value(), groupService.getAllPostInGroupSelf(currentPage,pageSize));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/{idGroup}/posts")
    public BaseResponse getAllPostInGroup(@PathVariable(name = "idGroup") String idGroup,@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseData(HttpStatus.OK.value(), groupService.getAllPostInGroup(idGroup,currentPage,pageSize));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @PostMapping
    public BaseResponse insert(@RequestBody Group group){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),groupService.insert(group));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }


    @PostMapping("/add-member")
    public BaseResponse addMemberIntoGroup(@RequestBody GroupMember groupMember){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),groupService.addMemberIntoGroup(groupMember));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @PostMapping("{idGroup}/request-join-group")
    public BaseResponse requestJoinGroup(@PathVariable(name = "idGroup") String idGroup){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),groupService.requestJoinGroup(idGroup));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    //user cancel or left group
    @DeleteMapping("{idGroup}/cancel-request-join-group")
    public BaseResponse CancelRequestJoinGroup(@PathVariable(name = "idGroup") String idGroup){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),groupService.cancelRequestJoinGroup(idGroup));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    //admin cancel request user Or remove user from group
    @DeleteMapping("{idGroup}/reject-request-join-group")
    public BaseResponse rejectRequestJoinGroup(@PathVariable(name = "idGroup") String idGroup,@RequestParam(name = "idUser") String idUser){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),groupService.rejectRequestJoinGroup(idGroup,idUser));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @GetMapping("{idGroup}/get-member")
    public BaseResponse getMemberInGroup(@PathVariable(name = "idGroup") String idGroup,@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size){
        try{
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseData(HttpStatus.OK.value(),groupService.getMemberInGroup(idGroup,1,currentPage,pageSize));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @GetMapping("{idGroup}/get-member-request")
    public BaseResponse getMemberRequestInGroup(@PathVariable(name = "idGroup") String idGroup,@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size){
        try{
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseData(HttpStatus.OK.value(),groupService.getMemberInGroup(idGroup,0,currentPage,pageSize));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @PutMapping("{idGroup}/confirm-member-request")
    public BaseResponse confirmMemberRequest(@PathVariable(name = "idGroup") String idGroup,@RequestBody GroupMember groupMember){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),groupService.confirmMemberRequest(groupMember));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @DeleteMapping("{idGroup}/cancel-member-request")
    public BaseResponse cancelMemberRequest(@PathVariable(name = "idGroup") String idGroup,@RequestBody GroupMember groupMember){
        try{
            return new ResponseMessage(HttpStatus.OK.value(),groupService.cancelMemberRequest(groupMember));
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(),"fail");
        }
    }

    @GetMapping("/{idGroup}/search-member")
    public BaseResponse searchMemberInGroup(@RequestParam(name = "search")String search,@PathVariable(name = "idGroup") String idGroup,@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseData(HttpStatus.OK.value(), groupService.searchMemberInGroup(search,idGroup,1,currentPage,pageSize));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

    @GetMapping("/{idGroup}/search-member-request")
    public BaseResponse searchMemberRequestInGroup(@RequestParam(name = "search")String search,@PathVariable(name = "idGroup") String idGroup,@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseData(HttpStatus.OK.value(), groupService.searchMemberInGroup(search,idGroup,0,currentPage,pageSize));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "fail");
        }
    }

}
