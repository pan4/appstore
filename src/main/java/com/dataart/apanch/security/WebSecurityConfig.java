package com.dataart.apanch.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: according to code convention it's not a good practice to have lines more than 120 chars
        http.authorizeRequests().antMatchers("/login**", "/", "/category-*", "/app-*", "/download/**").access("hasRole('USER') or hasRole('DEVELOPER')")
                .and().authorizeRequests().antMatchers("/new").access("hasRole('DEVELOPER')")
                .and().formLogin().loginPage("/login").loginProcessingUrl("/loginAction").permitAll()
                .and().logout().logoutSuccessUrl("/login").permitAll()
                .and().csrf().disable().exceptionHandling().accessDeniedPage("/Access_Denied");
        http.headers().contentTypeOptions().disable();
    }
}
