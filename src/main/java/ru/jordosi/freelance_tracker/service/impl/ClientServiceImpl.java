package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.client.ClientCreateDto;
import ru.jordosi.freelance_tracker.dto.client.ClientDto;
import ru.jordosi.freelance_tracker.dto.client.ClientUpdateDto;
import ru.jordosi.freelance_tracker.exception.ResourceNotFoundException;
import ru.jordosi.freelance_tracker.model.Client;
import ru.jordosi.freelance_tracker.repository.ClientRepository;
import ru.jordosi.freelance_tracker.service.AccessControlService;
import ru.jordosi.freelance_tracker.service.ClientService;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final AccessControlService accessControlService;

    @Override
    @Transactional(readOnly = true)
    public Page<ClientDto> getClientsForUser(Long userId, boolean includeGlobal, Pageable pageable) {
        return clientRepository.findAccessibleClients(userId, includeGlobal, pageable)
                .map(ClientDto::from);
    }

    @Override
    @Transactional
    public ClientDto createClient(ClientCreateDto  clientCreateDto, Long currentUserId, boolean isAdmin) {
        Client client = Client.builder()
                .name(clientCreateDto.getName())
                .email(clientCreateDto.getEmail())
                .contactInfo(clientCreateDto.getContactInfo())
                .createdBy(currentUserId)
                .isGlobal(isAdmin && clientCreateDto.isMakeGlobal())
                .build();
        return toDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    public ClientDto updateClient(Long clientId, ClientUpdateDto clientDto, Long currentUserId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + clientId));
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
        client.setContactInfo(clientDto.getContactInfo());

        return ClientDto.from(clientRepository.save(client));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ClientDto makeClientGlobal(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + clientId));
        client.setGlobal(true);
        client.setCreatedBy(null);

        return ClientDto.from(clientRepository.save(client));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto getClientDetails(Long clientId, Long currentUserId) {
        Client client = clientRepository.findByIdAndAccessible(clientId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found client or access denied"));
        return ClientDto.from(client);
    }

    @Override
    @Transactional
    public void archiveClient(Long clientId, Long currentUserId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + clientId));
        accessControlService.checkClientModificationAccess(client, currentUserId);

        client.setArchived(true);
        clientRepository.save(client);
    }

    private ClientDto toDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .createdAt(client.getCreatedAt())
                .createdBy(client.getCreatedBy())
                .isGlobal(client.isGlobal())
                .build();
    }
}
