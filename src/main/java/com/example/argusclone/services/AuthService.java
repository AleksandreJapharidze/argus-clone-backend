package com.example.argusclone.services;

import com.example.argusclone.entities.User;
import com.example.argusclone.exceptions.JwtGenerationException;
import com.example.argusclone.util.JwtUtil;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthService(UserDetailsService userDetailsService,
                       JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public String getToken(String personalId) {
        User user = (User) userDetailsService.loadUserByUsername(personalId);
        Map<String, Object> claims = getAllClaims(user);

        return jwtUtil.generateToken(personalId, claims);
    }

    private Map<String, Object> getAllClaims(User user) {
        String role = String.valueOf(user.getRole());
        if (role == null) {
            throw new JwtGenerationException("User is without role.");
        }

        if ("ADMIN".equals(role)) {
            return getAdminClaims();
        }

        return "INSTRUCTOR".equals(role) ? getInstructorClaims(user) : getStudentClaims(user);
    }

    private Map<String, Object> getStudentClaims(User user) {
        Integer roleId = user.getRoleId();
        return Map.of("role", "STUDENT", "roleId", roleId);
    }

    private Map<String, Object> getInstructorClaims(User user) {
        Integer roleId = user.getRoleId();
        return Map.of("role", "INSTRUCTOR", "roleId", roleId);
    }

    private Map<String, Object> getAdminClaims() {
        return Map.of("role", "ADMIN");
    }
}
