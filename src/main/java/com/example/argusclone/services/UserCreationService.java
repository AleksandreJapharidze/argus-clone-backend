package com.example.argusclone.services;

import com.example.argusclone.dtos.user.AdminDto;
import com.example.argusclone.dtos.user.UserDto;
import com.example.argusclone.entities.User;
import com.example.argusclone.enums.Role;
import com.example.argusclone.exceptions.AlreadyPresentException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.repositories.InstructorRepository;
import com.example.argusclone.repositories.StudentRepository;
import com.example.argusclone.repositories.UserDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCreationService {
    private final UserDetailsRepository userDetailsRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCreationService(UserDetailsRepository userDetailsRepository,
                               StudentRepository studentRepository,
                               InstructorRepository instructorRepository,
                               PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createUser(UserDto userDto, Integer roleId) {
        if (userDetailsRepository.existsByPersonalId(userDto.personalId())) {
            throw new AlreadyPresentException("User already exists");
        }

        User user = new User();
        user.setPersonalId(userDto.personalId());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        Role role = Role.valueOf(userDto.role().toUpperCase());
        if (role.equals(Role.ADMIN)) {
            throw new IllegalArgumentException("Admin role is not allowed in this request");
        }

        user.setRole(role);
        return saveUser(user, roleId);
    }

    public String createAdmin(AdminDto adminDto) {
        if (userDetailsRepository.existsByPersonalId(adminDto.personalId())) {
            throw new AlreadyPresentException("Someone with this personal id already exists");
        }

        User user = new User();
        user.setPersonalId(adminDto.personalId());
        user.setPassword(passwordEncoder.encode(adminDto.password()));
        user.setRole(Role.ADMIN);
        userDetailsRepository.save(user);
        return "Admin user created successfully: " + user.getPersonalId();
    }

    private String saveUser(User user, Integer roleId) {
        Role role = user.getRole();
        return switch (role) {
            case STUDENT -> saveUserForStudent(user, roleId);
            case INSTRUCTOR -> saveUserForInstructor(user, roleId);
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }

    private String saveUserForStudent(User user, Integer roleId) {
        if (studentRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Student with an id of " + roleId + " not found");
        }

        if (userDetailsRepository.existsByRoleAndRoleId(user.getRole(), roleId)) {
            throw new AlreadyPresentException("Student already has a user");
        }

        user.setRoleId(roleId);
        userDetailsRepository.save(user);

        return "Student user created successfully: " + user.getPersonalId();
    }

    private String saveUserForInstructor(User user, Integer roleId) {
        if (!instructorRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Instructor with an id of " + roleId + " not found");
        }

        if (userDetailsRepository.existsByRoleAndRoleId(user.getRole(), roleId)) {
            throw new AlreadyPresentException("Instructor already has a user");
        }

        user.setRoleId(roleId);
        userDetailsRepository.save(user);

        return "Instructor user created successfully: " + user.getPersonalId();
    }
}
