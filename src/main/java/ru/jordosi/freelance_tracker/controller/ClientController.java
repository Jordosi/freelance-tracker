package ru.jordosi.freelance_tracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jordosi.freelance_tracker.dto.client.ClientCreateDto;
import ru.jordosi.freelance_tracker.dto.client.ClientDto;
import ru.jordosi.freelance_tracker.dto.client.ClientUpdateDto;
import ru.jordosi.freelance_tracker.security.CurrentUserProvider;
import ru.jordosi.freelance_tracker.service.ClientService;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientCreateDto clientCreateDto,
                                                  @RequestParam(defaultValue = "false") boolean makeGlobal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientCreateDto, currentUserProvider.getCurrentUserId(), makeGlobal));
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> getClients(@RequestParam(defaultValue = "true") boolean includeGlobal,
                                      @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(clientService.getClientsForUser(userId, includeGlobal, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClient(@PathVariable Long id) {
        Long userId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(clientService.getClientDetails(id,  userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody ClientUpdateDto clientUpdateDto) {
        Long userId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(clientService.updateClient(id, clientUpdateDto, userId));
    }

    @PostMapping("/{id}/make-global")
    public ResponseEntity<ClientDto> makeGlobal(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.makeClientGlobal(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> archiveClient(@PathVariable Long id) {
        Long userId = currentUserProvider.getCurrentUserId();
        clientService.archiveClient(id, userId);
        return ResponseEntity.noContent().build();
    }
}
