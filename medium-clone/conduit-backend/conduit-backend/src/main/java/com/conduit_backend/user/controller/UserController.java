package com.conduit_backend.user.controller;

import com.conduit_backend.user.dto.*;
import com.conduit_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Register: POST /api/users
    @PostMapping("/users")
    public ResponseEntity<Map<String, UserResponse>> register(
            @Valid @RequestBody UserRegisterRequest request) {
        UserResponse resp = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("user", resp));
    }

    // Login: POST /api/users/login
    @PostMapping("/users/login")
    public ResponseEntity<Map<String, UserResponse>> login(
            @Valid @RequestBody UserLoginRequest request) {
        UserResponse resp = userService.login(request);
        return ResponseEntity.ok(Map.of("user", resp));
    }

    // Get current user: GET /api/user  (JWT required)
    @GetMapping("/user")
    public ResponseEntity<Map<String, UserResponse>> getCurrentUser(Principal principal) {
        String username = principal.getName(); // set by JwtAuthenticationFilter
        UserResponse resp = userService.getCurrentUser(username);
        return ResponseEntity.ok(Map.of("user", resp));
    }

    // Update user: PUT /api/user  (JWT required)
    @PutMapping("/user")
    public ResponseEntity<Map<String, UserResponse>> updateUser(
            Principal principal,
            @Valid @RequestBody UpdateUserRequest request) {
        String username = principal.getName();
        UserResponse resp = userService.updateUser(username, request);
        return ResponseEntity.ok(Map.of("user", resp));
    }

    // ✅ Logout Endpoint (JWT Required)
    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        return userService.logout(authHeader);
    }
}