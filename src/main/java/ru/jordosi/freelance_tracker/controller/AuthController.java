package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.LoginRequest;
import ru.jordosi.freelance_tracker.dto.RegisterRequest;
import ru.jordosi.freelance_tracker.dto.UserDto;
import ru.jordosi.freelance_tracker.security.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        return authService.getCurrentUser();
    }
}
