package com.conduit_backend.profile.service;

import com.conduit_backend.profile.dto.ProfileResponse;


public interface ProfileService {
    ProfileResponse getProfileByUsername(String username);
    ProfileResponse followUser(String username);
    ProfileResponse unfollowUser(String username);
}
