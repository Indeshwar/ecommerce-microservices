package com.demo.dto;




import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "User must enter fullName")
    private String fullName;
    @NotBlank(message = "userName can not be empty")
    private String userName;
    @NotBlank(message = "Password can not be empty")
    @Size(min=5 , message = "Password must be at least 5 characters long")
    private String password;
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Enter valid phone number")
    private String phoneNumber;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
