package com.demo.service;

import com.demo.dto.UserRequest;
import com.demo.entity.User;
import com.demo.exception.UserNotFoundException;
import com.demo.filter.JWTTokenHelper;
import com.demo.filter.RSAProperties;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Authentication authentication;
    @Mock
    private DaoAuthenticationProvider daoAuthenticationProvider;
    @Mock
    RSAProperties rsaProperties;
    @Mock
    private JWTTokenHelper jwtTokenHelper;
    @InjectMocks
    private UserService userService;


    @Test
    void testLoadUserByUsername(){
        User user = new User();
        user.setUserName("hari");
        //mock the repo
        when(userRepository.getByUserName("hari")).thenReturn(user);

        //Call the test method
        UserDetails userDetails = userService.loadUserByUsername("hari");

        //verify
        verify(userRepository, times(1)).getByUserName(user.getUserName());
        //Assert
        assertEquals(user.getUserName(), userDetails.getUsername());
    }

    @Test
    void testUserLogInFail() throws NoSuchAlgorithmException, InvalidKeyException {
        UserRequest request = new UserRequest();
        request.setUserName("hari");
        request.setPassword("wrongPassword");

        //Mock the DauAuthenticationProvider
        when(daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName()
                ,request.getPassword()))).thenThrow(new RuntimeException("Bad Credential"));

        //Execute the method
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()->{
            userService.LonIn(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(daoAuthenticationProvider, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenHelper, never()).generateToken(anyString());
    }

    @Test
    void testLogInSuccess() throws NoSuchAlgorithmException, InvalidKeyException {
        UserRequest request = new UserRequest();
        request.setUserName("hari");
        request.setPassword("hari123");

        //mock the behaviour
        when(daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName()
                ,request.getPassword()))).thenReturn(authentication);
        when(jwtTokenHelper.generateToken("hari")).thenReturn("testToken");

        //Execute the test method
        String token = userService.LonIn(request);

        //assert and verify
        assertEquals("testToken", token);
        verify(daoAuthenticationProvider, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenHelper, times(1)).generateToken(anyString());
    }

}
