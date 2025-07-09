package ru.jordosi.freelance_tracker.security.service;

import org.springframework.http.ResponseEntity;
import ru.jordosi.freelance_tracker.dto.LoginRequest;
import ru.jordosi.freelance_tracker.dto.RegisterRequest;
import ru.jordosi.freelance_tracker.dto.UserDto;

public interface AuthService {
    ResponseEntity<?> authenticate(LoginRequest loginRequest);
    ResponseEntity<?> register(RegisterRequest registerRequest);
    ResponseEntity<UserDto> getCurrentUser();
}