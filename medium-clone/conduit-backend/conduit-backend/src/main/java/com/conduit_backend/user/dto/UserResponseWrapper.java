package com.conduit_backend.user.dto;

import lombok.Data;

@Data
public class UserResponseWrapper {
    private UserResponse user;

    public UserResponseWrapper(UserResponse user) {
        this.user = user;
    }
}
