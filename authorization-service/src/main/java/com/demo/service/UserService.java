package com.demo.service;

import com.demo.entity.User;
import com.demo.entity.UserPrincipal;
import com.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@NoArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUserName(username);
        if(user == null){
            new UsernameNotFoundException("User Not Found");
        }
        return new UserPrincipal(user);
    }
}
