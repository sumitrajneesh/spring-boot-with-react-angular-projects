package com.conduit_backend.user.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String email;
    private String username;
    private String bio;
    private String image;
    private String password;
}
