package com.demo.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JWTFilterTest {
    @Autowired
    private JWTTokenHelper jwtTokenHelper;
    @Test
    void test(){
        System.out.println("This is demo test");
    }


    @Test
    void testGenerateTokenSuccess() throws NoSuchAlgorithmException, InvalidKeyException {
        String token = jwtTokenHelper.generateToken("hari");
    }
}
