package com.demo;

import com.demo.entity.Role;
import com.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AuthorizationApplication implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Role user = new Role("USER");
        Role admin = new Role("ADMIN");
        roleRepository.saveAll(List.of(admin, user));

    }
}