package ru.jordosi.freelance_tracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import ru.jordosi.freelance_tracker.dto.user.UserDto;
import ru.jordosi.freelance_tracker.dto.user.UserUpdateDto;
import ru.jordosi.freelance_tracker.model.User;

public interface UserService {
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto getCurrentUser(UserDetails userDetails);
    UserDto getUserById(Long id);
    UserDto updateCurrentUser(UserDetails userDetails, UserUpdateDto userUpdateDto);
    void deleteUser(Long id);
    UserDto changeUserRole(Long id, User.Role newRole);
}
