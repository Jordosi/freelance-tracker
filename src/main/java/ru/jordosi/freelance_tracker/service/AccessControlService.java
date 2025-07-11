package ru.jordosi.freelance_tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.jordosi.freelance_tracker.model.Client;
import ru.jordosi.freelance_tracker.security.CurrentUserProvider;

@Service
@RequiredArgsConstructor
public class AccessControlService {
    private final CurrentUserProvider currentUserProvider;
    public void checkClientModificationAccess(Client client, Long userId) {
        if (client.isGlobal() && !currentUserProvider.isAdmin()) {
            throw new AccessDeniedException("Only admin can modify global clients");
        }

        if (!client.getCreatedBy().equals(userId)) {
            throw new AccessDeniedException("You can only modify your own clients");
        }
    }
}
