package com.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Slf4j
public class JWTVerifierFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JWTVerifierFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String userName = request.getHeader("userName");
        String authority = request.getHeader("authority");

//        if(authHeader == null && !authHeader.startsWith("Bearer")){
//            filterChain.doFilter(request, response);
//            return;
//        }
        log.info("Auth Header is : {} ", authHeader);
        log.info("userName is : {}", userName);
        log.info("authority is : {}", authority);

        if(authority != null){
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Arrays.stream(authority.split(","))
                    .map(String:: trim) // Trim any extra space
                    .distinct() // Remove the duplicate
                    .filter(a -> !a.isEmpty()) //Filter the nonempty value
                    .map(SimpleGrantedAuthority:: new) //Map to SimpleGrantedAuthority using Method reference
                    .collect(Collectors.toSet());

            //Create Authentication object
            Authentication authentication = new UsernamePasswordAuthenticationToken(userName, null, simpleGrantedAuthorities);
            //Set the authentication object in the Security Context
            //So that subsequent filter can access it
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }


        filterChain.doFilter(request, response);
    }
}
