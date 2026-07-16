package com.example.argusclone.services;

import com.example.argusclone.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String personalId) {
        return userDetailsRepository.findByPersonalId(personalId).orElseThrow(
                () -> new UsernameNotFoundException("User with username: " + personalId + " not found")
        );
    }
}
