package com.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LogInRequest {
    @NotBlank(message = "userName can not be empty")
    private String userName;
    @NotBlank(message = "Invalid password")
    @Size(min=5 , message = "Invalid password")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
