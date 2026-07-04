package org.example.model;

import lombok.Data;

@Data
public class UserEntity {
    private String fullName;

    private boolean isAdult;

    public UserEntity(String fullName, boolean isAdult) {
        this.fullName = fullName;
        this.isAdult = isAdult;
    }
}
