package com.demo.controller;

import com.demo.dto.LogInRequest;
import com.demo.dto.UserRequest;
import com.demo.dto.UserResponse;
import com.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/validConnection")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequest request){
        String response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@Valid @RequestBody LogInRequest request){
        String response = userService.LonIn(request);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
}
