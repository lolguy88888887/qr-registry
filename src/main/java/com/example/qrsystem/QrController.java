package com.example.qrsystem;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class QrController {

    private final UserController userController;

    public QrController(UserController userController) {
        this.userController = userController;
    }

    @MessageMapping("/scan")
    @SendTo("/topic/qr")
    public String handleScan(String payload) {
        String scannedId = payload.trim();
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        // Update user status and last scanned timestamp
        userController.markUserAsScanned(scannedId, timeNow);

        // Return the scanned ID so WebSocket broadcasts it to all open pages
        return scannedId;
    }
}