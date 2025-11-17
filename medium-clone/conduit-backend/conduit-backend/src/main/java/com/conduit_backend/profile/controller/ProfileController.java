package com.conduit_backend.profile.controller;

import com.conduit_backend.profile.dto.ProfileResponse;
import com.conduit_backend.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@Tag(
        name = "Profile API",
        description = "Endpoints for viewing, following, and unfollowing user profiles"
)
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // ------------------------------------------------------------
    // GET PROFILE (public)
    // ------------------------------------------------------------
    @Operation(
            summary = "Get profile",
            description = "Fetches profile details of a user by username",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ProfileResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/{username}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getProfileByUsername(username));
    }

    // ------------------------------------------------------------
    // FOLLOW USER (JWT required)
    // ------------------------------------------------------------
    @Operation(
            summary = "Follow a user",
            description = "Authenticated user follows the specified username",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User followed successfully",
                            content = @Content(schema = @Schema(implementation = ProfileResponse.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing token"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PostMapping("/{username}/follow")
    public ResponseEntity<ProfileResponse> followUser(@PathVariable String username) {
        return ResponseEntity.ok(profileService.followUser(username));
    }

    // ------------------------------------------------------------
    // UNFOLLOW USER (JWT required)
    // ------------------------------------------------------------
    @Operation(
            summary = "Unfollow a user",
            description = "Authenticated user unfollows the specified username",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User unfollowed successfully",
                            content = @Content(schema = @Schema(implementation = ProfileResponse.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing token"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ProfileResponse> unfollowUser(@PathVariable String username) {
        return ResponseEntity.ok(profileService.unfollowUser(username));
    }
}