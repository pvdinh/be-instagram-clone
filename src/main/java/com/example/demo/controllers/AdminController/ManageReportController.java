package com.example.demo.controllers.AdminController;

import com.example.demo.models.report.Report;
import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/manage-report")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ManageReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping
    public BaseResponse findAllPageable(@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size){
        try{
            int currentPage = page.orElse(0);
            int pageSize = size.orElse(10);
            List<Report> reports = reportService.findAllPageable(currentPage,pageSize);
            return new ResponseData(HttpStatus.OK.value(),reports,reportService.findAll().size());
        }catch (Exception e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }
}
