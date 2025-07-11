package ru.jordosi.freelance_tracker.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    @Size(min=8)
    private String password;

    @NotNull
    @NotBlank
    @Email
    private String email;
}
