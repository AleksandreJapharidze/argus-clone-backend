package com.example.argusclone.services;

import com.example.argusclone.entities.User;
import com.example.argusclone.exceptions.JwtGenerationException;
import com.example.argusclone.util.JwtUtil;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {
    private final UserDetailsService userDetailsService;
    private final CourseInstructorService courseInstructorService;
    private final CourseStudentService courseStudentService;
    private final JwtUtil jwtUtil;

    public AuthService(UserDetailsService userDetailsService,
                       CourseInstructorService courseInstructorService,
                       CourseStudentService courseStudentService,
                       JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.courseInstructorService = courseInstructorService;
        this.courseStudentService = courseStudentService;
        this.jwtUtil = jwtUtil;
    }

    public String getToken(String personalId) {
        User user = (User) userDetailsService.loadUserByUsername(personalId);
        Map<String, Object> claims = getAllClaims(user);

        return jwtUtil.generateToken(personalId, claims);
    }

    private Map<String, Object> getAllClaims(User user) {
//        return !user.getRole().equals("ROLE_ADMIN") ? user.getRole().equals("ROLE_INSTRUCTOR") ? getInstructorClaims(user) : getStudentClaims(user) : getAdminClaims(user);
        String role = String.valueOf(user.getRole());
        if (role == null) {
            throw new JwtGenerationException("User is without role.");
        }

        if ("ROLE_ADMIN".equals(role)) {
            return getAdminClaims(user);
        }

        return "ROLE_INSTRUCTOR".equals(role) ? getInstructorClaims(user) : getStudentClaims(user);
    }

    private Map<String, Object> getStudentClaims(User user) {
        Integer roleId = user.getRoleId();
        List<Integer> courseIds = courseStudentService.getCourseIdsByStudentId(roleId);
        return Map.of("role", "ROLE_STUDENT", "roleId", roleId, "courseIds", courseIds);
    }

    private Map<String, Object> getInstructorClaims(User user) {
        Integer roleId = user.getRoleId();
        List<Integer> courseIds = courseInstructorService.getCourseIdsByInstructorId(roleId);
        return Map.of("role", "ROLE_INSTRUCTOR", "roleId", roleId, "courseIds", courseIds);
    }

    private Map<String, Object> getAdminClaims(User user) {
        return Map.of("role", "ROLE_ADMIN");
    }
}
