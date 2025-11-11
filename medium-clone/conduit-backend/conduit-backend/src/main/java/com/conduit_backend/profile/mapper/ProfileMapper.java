package com.conduit_backend.profile.mapper;

import com.conduit_backend.profile.dto.ProfileDto;
import com.conduit_backend.profile.dto.ProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileResponse toProfileResponse(ProfileDto profile, boolean following) {
        ProfileResponse.ProfileData profileData = new ProfileResponse.ProfileData(
                profile.getUser().getUsername(),
                profile.getBio(),
                profile.getImage(),
                following
        );
        return new ProfileResponse(profileData);
    }
}
