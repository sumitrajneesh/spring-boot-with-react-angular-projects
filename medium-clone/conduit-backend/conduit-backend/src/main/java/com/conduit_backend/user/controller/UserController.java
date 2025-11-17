package com.conduit_backend.user.controller;

import com.conduit_backend.user.dto.*;
import com.conduit_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@Tag(name = "User APIs", description = "Endpoints for User Registration, Login, Profile & Logout")
public class UserController {

    private final UserService userService;

    // ------------------------------------------------------------
    // REGISTER USER
    // ------------------------------------------------------------
    @Operation(
            summary = "Register new user",
            description = "Creates a new user account and returns the user object with JWT token"

    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "user": {
                                        "username": "john_doe",
                                        "email": "john@example.com",
                                        "token": "jwt-token-123",
                                        "bio": null,
                                        "image": null
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/users")
    public ResponseEntity<Map<String, UserResponse>> register(
            @Valid @RequestBody UserRegisterRequest request) {

        UserResponse resp = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("user", resp));
    }

    // ------------------------------------------------------------
    // LOGIN USER
    // ------------------------------------------------------------
    @Operation(
            summary = "Login user",
            description = "Validates user credentials and returns JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully logged in",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "user": {
                                        "username": "john_doe",
                                        "email": "john@example.com",
                                        "token": "jwt-token-123"
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/users/login")
    public ResponseEntity<Map<String, UserResponse>> login(
            @Valid @RequestBody UserLoginRequest request) {

        UserResponse resp = userService.login(request);
        return ResponseEntity.ok(Map.of("user", resp));
    }

    // ------------------------------------------------------------
    // GET CURRENT USER
    // ------------------------------------------------------------
    @Operation(
            summary = "Get current user profile",
            description = "Returns the authenticated user details. *JWT token required*",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile loaded"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token")
    })
    @GetMapping("/user")
    public ResponseEntity<Map<String, UserResponse>> getCurrentUser(Principal principal) {
        String username = principal.getName();
        UserResponse resp = userService.getCurrentUser(username);
        return ResponseEntity.ok(Map.of("user", resp));
    }

    // ------------------------------------------------------------
    // UPDATE USER
    // ------------------------------------------------------------
    @Operation(
            summary = "Update user profile",
            description = "Updates authenticated user's profile. *JWT token required*",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid token")
    })
    @PutMapping("/user")
    public ResponseEntity<Map<String, UserResponse>> updateUser(
            Principal principal,
            @Valid @RequestBody UpdateUserRequest request) {

        String username = principal.getName();
        UserResponse resp = userService.updateUser(username, request);
        return ResponseEntity.ok(Map.of("user", resp));
    }

    // ------------------------------------------------------------
    // LOGOUT
    // ------------------------------------------------------------
    @Operation(
            summary = "Logout user",
            description = "Invalidates user's JWT. *JWT token required*",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged out successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid token")
    })
    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String authHeader) {

        return userService.logout(authHeader);
    }
}