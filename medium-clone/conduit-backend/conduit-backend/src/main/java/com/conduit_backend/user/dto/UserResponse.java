package com.conduit_backend.user.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String email;     // returned only for auth routes
    private String token;     // JWT token (login/register/update)
    private String username;
    private String bio;
    private String image;
    private Boolean following; // optional, used in article author representation
}
