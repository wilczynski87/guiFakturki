package com.fakturki.gui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyGlobalSecurityConfig 
{
    
    // @Autowired
    // protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //     auth
    //     .inMemoryAuthentication()
    //     .withUser("user")
    //     .password("{noop}password")
    //     .roles("USER")
    //     .and()
    //     .withUser("admin")
    //     .password("{noop}password")
    //     .roles("ADMIN")
    //     ;
    // }

    @Bean 
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();
        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder().encode("password"))
            .roles("ADMIN")
            .build();
        UserDetails test = User.withUsername("test")
            .password(passwordEncoder().encode("test"))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user, test, admin);
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http.csrf()
    //         .disable()
    //         .authorizeRequests()
    //         .antMatchers("/admin/**")
    //         .hasRole("ADMIN")
    //         .antMatchers("/anonymous*")
    //         .anonymous()
    //         .antMatchers("/login*")
    //         .permitAll()
    //         .anyRequest()
    //         .authenticated()
    //         .and()
    //         .formLogin()
    //         .loginPage("/login.html")
    //         .loginProcessingUrl("/perform_login")
    //         .defaultSuccessUrl("/homepage.html", true)
    //         .failureUrl("/login.html?error=true")
    //         .failureHandler(authenticationFailureHandler())
    //         .and()
    //         .logout()
    //         .logoutUrl("/perform_logout")
    //         .deleteCookies("JSESSIONID")
    //         .logoutSuccessHandler(logoutSuccessHandler());
    //         return http.build();
    // }
}

