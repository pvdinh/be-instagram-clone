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



}
