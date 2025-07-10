package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.UserDto;
import ru.jordosi.freelance_tracker.dto.UserUpdateDto;
import ru.jordosi.freelance_tracker.exception.ResourceNotFoundException;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.UserRepository;
import ru.jordosi.freelance_tracker.service.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser(UserDetails userDetails) {
        User user = (User) userDetails;
        return UserDto.from(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserDto updateCurrentUser(UserDetails userDetails, UserUpdateDto userUpdateDto) {
        User user = (User) userDetails;

        user.setUsername(userUpdateDto.getUsername());
        user.setEmail(userUpdateDto.getEmail());
        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        return UserDto.from(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SecurityContext context = SecurityContextHolder.getContext();
        User currentUser = (User) context.getAuthentication().getPrincipal();

        if (currentUser.getId().equals(user.getId())) {
            throw new IllegalStateException("You cannot delete yourself");
        }

        userRepository.delete(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto changeUserRole(Long id, User.Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRole(newRole);
        return UserDto.from(userRepository.save(user));
    }
}
