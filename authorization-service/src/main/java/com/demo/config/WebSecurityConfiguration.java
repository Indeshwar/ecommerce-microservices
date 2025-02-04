package com.demo.config;

import com.demo.filter.JWTAuthenticationVerifierFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfiguration {
    private UserDetailsService userDetailsService;
    private JWTAuthenticationVerifierFilter authenticationVerifierFilter;
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public WebSecurityConfiguration(UserDetailsService userDetailsService, JWTAuthenticationVerifierFilter authenticationVerifierFilter, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authenticationVerifierFilter = authenticationVerifierFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return  daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http.csrf(p->p.disable());
        http.authorizeHttpRequests(c -> c.requestMatchers("/api/v1/validConnection/*").permitAll()
                .anyRequest().authenticated())
                .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e-> e.authenticationEntryPoint(restAuthenticationEntryPoint))
                .addFilterBefore(authenticationVerifierFilter, UsernamePasswordAuthenticationFilter.class); //authenticationVerifierFilter execute before UsernamePasswordAuthenticationFilter
        return http.build();
    }

}
