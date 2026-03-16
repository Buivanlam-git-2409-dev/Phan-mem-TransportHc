package com.transporthc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transporthc.config.NotificationWebSocketHandler;

@Service
public class NotificationService {
    @Autowired
    private NotificationWebSocketHandler webSocketHandler;

    public void sendNotification(String message) {
        webSocketHandler.broadcast(message);
    }
}
