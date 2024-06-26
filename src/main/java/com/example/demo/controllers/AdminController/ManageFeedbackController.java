package com.example.demo.controllers.AdminController;

import com.example.demo.models.feedback.Feedback;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/manage-feedback")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ManageFeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    //http://localhost:8080/api/v1/admin/manage-feedback
    @GetMapping
    public BaseResponse findAllPageable(@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            List<Feedback> feedbacks = feedbackService.findAllPageable(currentPage, pageSize);
            return new ResponseData(HttpStatus.OK.value(), feedbacks, feedbackService.findAll().size());
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-feedback/{search}/search
    @GetMapping("/{search}/search")
    public BaseResponse findContainsByIdUserOrSubjectOrContent(@PathVariable(name = "search") String search, @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            List<Feedback> feedbacks = feedbackService.findContainsByIdUserOrSubjectOrContentPageable(search, currentPage, pageSize);
            return new ResponseData(HttpStatus.OK.value(), feedbacks, feedbackService.findContainsByIdUserOrSubjectOrContent(search).size());
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/manage-feedback/{id}/delete
    @DeleteMapping("/{id}/delete")
    public BaseResponse delete(@PathVariable(name = "id") String id) {
        try {
            return new ResponseMessage(HttpStatus.OK.value(), feedbackService.delete(id));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }


    //http://localhost:8080/api/v1/admin/manage-feedback/filter
    @GetMapping("/filter")
    public BaseResponse filterByTime(@RequestParam(name = "start") String start, @RequestParam(name = "end") String end, @RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            return new ResponseData(HttpStatus.OK.value(), feedbackService.filterByTimePageable(Long.parseLong(start), Long.parseLong(end), currentPage, pageSize), feedbackService.filterByTime(Long.parseLong(start), Long.parseLong(end)).size());
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

}
