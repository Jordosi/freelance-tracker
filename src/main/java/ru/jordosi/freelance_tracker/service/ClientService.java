package ru.jordosi.freelance_tracker.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.jordosi.freelance_tracker.dto.client.ClientCreateDto;
import ru.jordosi.freelance_tracker.dto.client.ClientDto;
import ru.jordosi.freelance_tracker.dto.client.ClientUpdateDto;

public interface ClientService {
    Page<ClientDto> getClientsForUser(Long userId, boolean includeGlobal, Pageable pageable);
    ClientDto createClient(ClientCreateDto dto, Long currentUserId, boolean isAdmin);
    ClientDto updateClient(Long clientId, ClientUpdateDto dto, Long  currentUserId);
    ClientDto getClientDetails(Long clientId, Long currentUserId);
    ClientDto makeClientGlobal(Long clientId);
    void archiveClient(Long clientId, Long currentUserId);
}
