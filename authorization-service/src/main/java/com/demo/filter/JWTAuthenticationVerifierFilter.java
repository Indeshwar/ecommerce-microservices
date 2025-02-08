package com.demo.filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
public class JWTAuthenticationVerifierFilter extends OncePerRequestFilter {
    private JWTTokenHelper jwtTokenHelper;
    private UserDetailsService userDetailsService;

    public JWTAuthenticationVerifierFilter(JWTTokenHelper jwtTokenHelper, UserDetailsService userDetailsService) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenHelper.getToken(request);

        if(token != null){
            String userName = jwtTokenHelper.getUserNameFromToken(token);
            if(userName != null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                if(jwtTokenHelper.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    request.setAttribute("userName", userDetails.getUsername());
                    request.setAttribute("authority", userDetails.getAuthorities());
                    request.setAttribute("jwt", token);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
