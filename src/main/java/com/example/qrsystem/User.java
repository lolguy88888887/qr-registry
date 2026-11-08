package com.example.qrsystem;

public class User {
    private String id;
    private String name;
    private String status;
    private String lastScanned;

    public User() {}

    public User(String id, String name, String status, String lastScanned) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.lastScanned = lastScanned;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLastScanned() { return lastScanned; }
    public void setLastScanned(String lastScanned) { this.lastScanned = lastScanned; }
}