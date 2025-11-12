package com.conduit_backend.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private ProfileData profile;

    // ✅ Constructor that initializes ProfileData properly
    public ProfileResponse(String username, String bio, String image, boolean isFollowing) {
        this.profile = new ProfileData(username, bio, image, isFollowing);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileData {
        private String username;
        private String bio;
        private String image;
        private boolean following;
    }
}
