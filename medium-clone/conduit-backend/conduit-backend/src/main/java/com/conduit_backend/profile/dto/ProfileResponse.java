package com.conduit_backend.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private ProfileData profile;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileData {
        private String username;
        private String bio;
        private String image;
        private boolean following;
    }
}