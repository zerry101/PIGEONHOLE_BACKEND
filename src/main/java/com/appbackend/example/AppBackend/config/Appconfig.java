package com.appbackend.example.AppBackend.config;


import com.appbackend.example.AppBackend.entities.Role;
import com.appbackend.example.AppBackend.entities.User;
import com.appbackend.example.AppBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class Appconfig {


    @Autowired
    private UserRepository userRepository;

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        User user1=new User();
//
//        user1.setEmail("xyz@gamil.com");
//        user1.setRole(Role.valueOf("ADMIN"));
//        user1.setPassword(passwordEncoder().encode("abc"));
//        user1.setFirstName("Zishan");
//        user1.setLastName("Shaikh");
//
//        userRepository.save(user1);
//        return new InMemoryUserDetailsManager(user1);
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("user not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws  Exception{
        return  builder.getAuthenticationManager();
    }
}

