package com.conduit_backend.user.mapper;

import com.conduit_backend.user.dto.UserResponse;
import com.conduit_backend.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // For auth endpoints: login/register/update → include token
    public static UserResponse toUserResponse(User user, String token) {
        return UserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .token(token)
                .following(false)// include JWT if needed
                .build();
    }

    // For embedding user inside article → no token
    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .following(false)
                .build();
    }
}
