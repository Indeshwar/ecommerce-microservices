package com.demo.service;

import com.demo.dto.UserRequest;
import com.demo.dto.UserResponse;
import com.demo.entity.Role;
import com.demo.entity.User;
import com.demo.entity.UserPrincipal;
import com.demo.exception.UserAlreadyExistException;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Lazy
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUserName(username);
        if(user == null){
            new UsernameNotFoundException("User Not Found");
        }
        return new UserPrincipal(user);
    }

    public String registerUser(UserRequest request){
        User existingUser = userRepository.getByUserName(request.getUserName());
        if(existingUser != null){
            throw new UserAlreadyExistException("User already exist");
        }
        User user = new User();
        user.setFullName(request.getFullName());
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        Role role = roleRepository.getByRoleName("USER");
        user.addRole(role);
        userRepository.save(user);
        return  "User Successfully created";
    }


}
