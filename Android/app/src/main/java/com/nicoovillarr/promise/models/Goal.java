package com.nicoovillarr.promise.models;

import java.time.LocalDateTime;

public class Goal {

    private String name;
    private String description;
    private LocalDateTime createdOn;
    private LocalDateTime completedOn;

    public Goal() {}

    public Goal(String name) {
        this.name = name;
        this.description = null;
        this.createdOn = LocalDateTime.now();
        this.completedOn = null;
    }

    public Goal(String name, String description, LocalDateTime createdOn, LocalDateTime completedOn) {
        this.name = name;
        this.description = description;
        this.createdOn = createdOn;
        this.completedOn = completedOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
    }
}
