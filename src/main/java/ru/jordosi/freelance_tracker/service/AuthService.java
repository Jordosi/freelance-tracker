package ru.jordosi.freelance_tracker.service;

import org.springframework.http.ResponseEntity;
import ru.jordosi.freelance_tracker.dto.auth.LoginRequest;
import ru.jordosi.freelance_tracker.dto.auth.RegisterRequest;
import ru.jordosi.freelance_tracker.dto.user.UserDto;

public interface AuthService {
    ResponseEntity<?> authenticate(LoginRequest loginRequest);
    ResponseEntity<?> register(RegisterRequest registerRequest);
    ResponseEntity<UserDto> getCurrentUser();
}