package com.portfolio.backend.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.portfolio.backend.entity.User;
import com.portfolio.backend.repository.UserRepository;

/**
 * Custom implementation of UserDetailsService for loading user-specific data.
 * This service is used by Spring Security to authenticate and authorize users
 * based on database records.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        /**
         * Constructor for dependency injection of the UserRepository.
         * 
         * @param userRepository the user repository
         */
        public CustomUserDetailsService(UserRepository userRepository) {
                this.userRepository = userRepository;
        }

        /**
         * Loads a user from the database by username and builds a UserDetails object
         * for Spring Security.
         * 
         * @param username the username to look up
         * @return UserDetails object containing user data and authorities (roles)
         * @throws UsernameNotFoundException if the user is not found
         */
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User not found with username: " + username));

                // Grant authority based on the user's role
                return new org.springframework.security.core.userdetails.User(
                                user.getUsername(),
                                user.getPassword(),
                                Collections.singletonList(
                                                new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())));
        }
}
