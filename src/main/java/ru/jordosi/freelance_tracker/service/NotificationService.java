package ru.jordosi.freelance_tracker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    public void sendNotification(String user, String message) {
        log.info("Sending notification to user: " + user +" \n"+message);
    }
}
