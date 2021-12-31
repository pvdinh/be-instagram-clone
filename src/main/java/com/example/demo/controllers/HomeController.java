package com.example.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class HomeController {
    @GetMapping
    public String home(){
        return "home";
    }
}
