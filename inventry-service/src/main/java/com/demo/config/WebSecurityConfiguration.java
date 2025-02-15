package com.demo.config;

import com.demo.filter.JWTVerifierFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    private JWTVerifierFilter jwtVerifierFilter;
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public WebSecurityConfiguration(JWTVerifierFilter jwtVerifierFilter, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.jwtVerifierFilter = jwtVerifierFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return  http.csrf(customizer->customizer.disable()) //Disable the csrf
                .authorizeHttpRequests(request-> request
                        .requestMatchers("/api/message").hasAuthority("ADMIN") //Allow ADMIN role to access
                        .requestMatchers("/api/inventory").hasAuthority("USER") //Allow USER role to access
                        .anyRequest().authenticated()) // Allows only authenticated requests
                .exceptionHandling(exception-> exception.authenticationEntryPoint(restAuthenticationEntryPoint)) //Throw the exception if the unauthenticated requests
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Make Session Stateless
                .addFilterBefore(jwtVerifierFilter, UsernamePasswordAuthenticationFilter.class) //Execute jwtVerifierFilter before UsernamePasswordAuthenticationFilter
                .build();
    }
}
