package ru.jordosi.freelance_tracker.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ClientCreateDto {
    @NotBlank private String name;
    @NotBlank @Email
    private String email;
    private String contactInfo;
    private boolean makeGlobal;
}
