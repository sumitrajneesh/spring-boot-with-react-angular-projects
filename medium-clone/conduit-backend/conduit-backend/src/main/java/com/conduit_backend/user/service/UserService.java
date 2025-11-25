package com.conduit_backend.user.service;

import com.conduit_backend.user.dto.*;
import com.conduit_backend.user.entity.User;
import org.springframework.http.ResponseEntity;


public interface UserService {
    UserResponse register(UserRegisterWrapper request);
    UserResponse login(UserLoginWrapper request);
    UserResponse getCurrentUser(String username);
    UserResponse updateUser(String username, UpdateUserRequest request);
    ResponseEntity<String> logout(String authHeader);
}
