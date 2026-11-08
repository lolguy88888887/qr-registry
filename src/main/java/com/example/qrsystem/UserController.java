package com.example.qrsystem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final String FILE_PATH = "users.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<User> users = new ArrayList<>();

    public UserController() {
        loadUsers();
    }

    private synchronized void loadUsers() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try {
                users = objectMapper.readValue(file, new TypeReference<List<User>>() {});
            } catch (IOException e) {
                e.printStackTrace();
                users = new ArrayList<>();
            }
        }
    }

    private synchronized void saveUsers() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public synchronized List<User> getAllUsers() {
        return users;
    }

    @PostMapping
    public synchronized User createUser(@RequestBody User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId("USR-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        }
        user.setStatus("DEACTIVE");
        user.setLastScanned("Never");
        users.add(user);
        saveUsers();
        return user;
    }

    @PostMapping("/scan")
    public synchronized User scanUser(@RequestBody User scanPayload) {
        String id = scanPayload.getId();
        if (id == null) return null;
        
        String cleanId = id.replaceAll("[\"']", "").trim();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        System.out.println("DEBUG: REST Scan received for ID -> [" + cleanId + "]");
        
        User matchedUser = null;
        for (User u : users) {
            if (u.getId() != null && u.getId().trim().equalsIgnoreCase(cleanId)) {
                u.setStatus("ACTIVE");
                u.setLastScanned(timestamp);
                matchedUser = u;
                System.out.println("DEBUG: Match found! User " + u.getId() + " updated to ACTIVE.");
            }
        }
        
        if (matchedUser != null) {
            saveUsers();
            return matchedUser;
        } else {
            System.out.println("DEBUG: NO MATCH FOUND for ID: " + cleanId);
            return null;
        }
    }

    public synchronized void markUserAsScanned(String id, String timestamp) {
        scanUser(new User(id, null, null, null));
    }
}