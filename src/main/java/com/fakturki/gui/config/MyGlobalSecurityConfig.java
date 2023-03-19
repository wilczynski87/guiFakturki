package com.fakturki.gui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class MyGlobalSecurityConfig 
{
    
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .inMemoryAuthentication()
        .withUser("user")
        .password("{noop}password")
        .roles("USER")
        .and()
        .withUser("admin")
        .password("{noop}password")
        .roles("ADMIN");
    }
}

