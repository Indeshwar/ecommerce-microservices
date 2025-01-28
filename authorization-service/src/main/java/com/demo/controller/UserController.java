package com.demo.controller;

import com.demo.dto.UserRequest;
import com.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorization")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/message")
    public String getMessage(){
        return "This is authorization Server";
    }

    @GetMapping("/user")
    public String getUser(){
        return "User is Ram";
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequest request){
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }
}
