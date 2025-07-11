package ru.jordosi.freelance_tracker.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.model.Client;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class ClientDto {
    private Long id;
    private String name;
    private String email;
    private String contactInfo;
    private LocalDateTime createdAt;
    private Boolean isGlobal;
    private Boolean isArchived;
    private Long createdBy;

    public static ClientDto from(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .contactInfo(client.getContactInfo())
                .createdAt(client.getCreatedAt())
                .createdBy(client.getCreatedBy())
                .isGlobal(client.isGlobal())
                .isArchived(client.isArchived())
                .build();
    }
}
