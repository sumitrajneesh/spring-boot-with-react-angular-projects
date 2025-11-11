package com.conduit_backend.user.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;
}