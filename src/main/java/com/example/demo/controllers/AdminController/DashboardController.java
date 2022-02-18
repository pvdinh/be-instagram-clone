package com.example.demo.controllers.AdminController;

import com.example.demo.response.BaseResponse;
import com.example.demo.response.ResponseData;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.example.demo.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    //http://localhost:8080/api/v1/admin/dashboard/overview
    @GetMapping("/overview")
    public BaseResponse statisticalOverview() {
        try {
            return new ResponseObject(HttpStatus.OK.value(), dashboardService.statisticalOverview());
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/percentage-auth
    @GetMapping("/percentage-auth")
    public BaseResponse percentageAuthUser() {
        try {
            return new ResponseObject(HttpStatus.OK.value(), dashboardService.percentageAuthUser());
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }

    //http://localhost:8080/api/v1/admin/year/chart-data
    @GetMapping("/{year}/chart-data")
    public BaseResponse chartData(@PathVariable(name = "year") String year) {
        try {
            return new ResponseObject<>(HttpStatus.OK.value(), dashboardService.chartData(Integer.parseInt(year)));
        } catch (Exception e) {
            return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "error");
        }
    }
}
