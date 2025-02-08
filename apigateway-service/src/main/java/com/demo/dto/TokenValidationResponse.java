package com.demo.dto;

import java.util.Set;

public class TokenValidationResponse {
    private String status;
    private boolean isAuthenticated;
    private String methodType;
    private String username;
    private Set<Authorities> authorities;
    private String token;

    public String getStatus() {
        return status;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public String getMethodType() {
        return methodType;
    }

    public String getUsername() {
        return username;
    }

    public Set<Authorities> getAuthorities() {
        return authorities;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "TokenValidationResponse{" +
                "status='" + status + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                ", methodType='" + methodType + '\'' +
                ", username='" + username + '\'' +
                ", authorities=" + authorities +
                ", token='" + token + '\'' +
                '}';
    }
}
