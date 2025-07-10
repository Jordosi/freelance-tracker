package ru.jordosi.freelance_tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    @NotBlank
    private String username;
    @NotBlank @Email
    private String email;
    @Size(min=8)
    private String password;
}
