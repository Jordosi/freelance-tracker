package ru.jordosi.freelance_tracker.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class LoginRequest {
    private String username;
    private String password;
}
