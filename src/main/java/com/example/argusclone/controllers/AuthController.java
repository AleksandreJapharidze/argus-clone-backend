package com.example.argusclone.controllers;

import com.example.argusclone.dtos.user.AdminDto;
import com.example.argusclone.dtos.user.AuthRequest;
import com.example.argusclone.dtos.user.UserDto;
import com.example.argusclone.services.AuthService;
import com.example.argusclone.services.UserCreationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Value("${admin.secret}")
    private String adminSecret;

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final UserCreationService userCreationService;

    public AuthController(AuthenticationManager authenticationManager,
                          AuthService authService,
                          UserCreationService userCreationService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.userCreationService = userCreationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.personalId(), authRequest.password())
        );

        return ResponseEntity.ok(authService.getToken(authRequest.personalId()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user-registration")
    public ResponseEntity<String> registerUser(@RequestParam Integer roleId, @RequestBody UserDto userDto) {
        return ResponseEntity.status(201).body(userCreationService.createUser(userDto, roleId));
    }

    @PostMapping("/admin-registration")
    public ResponseEntity<String> registerAdmin(@RequestParam String adminSecret, @RequestBody AdminDto adminDto) {
        if (!this.adminSecret.equals(adminSecret)) {
            return ResponseEntity.status(401).body("Invalid admin secret");
        }
        return ResponseEntity.status(201).body(userCreationService.createAdmin(adminDto));
    }
}
