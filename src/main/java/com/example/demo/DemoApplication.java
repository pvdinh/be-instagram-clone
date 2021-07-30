package com.example.demo;

import com.example.demo.models.Account;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RequestMapping("/login")
@Api(value = "/login", tags = "Login", description = "Login")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostMapping
    @ApiOperation(value = "Đăng nhập để lấy token")
    public String login(@RequestBody Account account){
        return "authorization";
    }

}
