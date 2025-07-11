package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.auth.AuthResponse;
import ru.jordosi.freelance_tracker.dto.user.UserDto;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.UserRepository;
import ru.jordosi.freelance_tracker.dto.auth.LoginRequest;
import ru.jordosi.freelance_tracker.dto.auth.RegisterRequest;
import ru.jordosi.freelance_tracker.service.AuthService;
import ru.jordosi.freelance_tracker.service.JwtService;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public ResponseEntity<?> authenticate(LoginRequest loginRequest) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            String token = jwtService.generateToken(Map.of(
                    "roles", auth.getAuthorities()
                            .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
            ), (UserDetails) auth.getPrincipal());

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(AuthResponse.builder()
                            .token(token)
                            .username(auth.getName())
                            .roles(auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                            .build());
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                savedUser.getUsername(),
                null,
                Collections.singleton(new SimpleGrantedAuthority(savedUser.getRole().name()))
        );

        String jwtToken = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        savedUser.getUsername(),
                        savedUser.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority(savedUser.getRole().name()))
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .body(AuthResponse.builder()
                        .token(jwtToken)
                        .username(savedUser.getUsername())
                        .roles(Collections.singletonList(savedUser.getRole().name()))
                        .build());
    }

    @Override
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(UserDto.from(user));
    }
}
