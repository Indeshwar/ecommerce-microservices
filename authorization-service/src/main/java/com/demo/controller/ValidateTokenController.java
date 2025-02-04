package com.demo.controller;

import com.demo.dto.TokenValidationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/validateToken")
public class ValidateTokenController {
    @GetMapping
    public ResponseEntity<TokenValidationResponse> validateToken(HttpServletRequest request){
        String userName = (String) request.getAttribute("userName");
        Set<SimpleGrantedAuthority> grantedAuthorities = (Set)request.getAttribute("authority");

        TokenValidationResponse response = new TokenValidationResponse();
        response.setStatus("OK");
        response.setUsername(userName);
        response.setAuthorities(grantedAuthorities);
        response.setAuthenticated(true);
        response.setMethodType(HttpMethod.GET.toString());

        return ResponseEntity.ok(response);

    }
}
