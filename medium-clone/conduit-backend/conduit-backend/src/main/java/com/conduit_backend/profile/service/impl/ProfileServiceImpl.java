package com.conduit_backend.profile.service.impl;

import com.conduit_backend.config.JwtTokenProvider;
import com.conduit_backend.profile.dto.ProfileDto;
import com.conduit_backend.profile.dto.ProfileResponse;
import com.conduit_backend.profile.entity.Profile;
import com.conduit_backend.profile.mapper.ProfileMapper;
import com.conduit_backend.profile.respository.ProfileRepository;
import com.conduit_backend.profile.service.ProfileService;
import com.conduit_backend.user.entity.User;
import com.conduit_backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProfileMapper profileMapper;

    // ✅ Get current logged-in user from JWT
//    private User getCurrentUser() {
//        String email = jwtTokenProvider.getUsernameFromToken(
//                (String) org.springframework.security.core.context.SecurityContextHolder
//                        .getContext()
//                        .getAuthentication()
//                        .getCredentials()
//        );
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
//    }

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        // Spring Security automatically sets the username as authentication.getName()
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    // ✅ Get profile by username
    @Override
    public ProfileResponse getProfileByUsername(String username) {
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Profile profile = profileRepository.findByUser(targetUser)
                .orElseGet(() -> {
                    Profile newProfile = new Profile();
                    newProfile.setUser(targetUser);
                    newProfile.setBio("");
                    newProfile.setImage("https://example.com/default-image.jpg");
                    return profileRepository.save(newProfile);
                });

        boolean isFollowing = false;
        if (username != null) {
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            Profile currentProfile = profileRepository.findByUser(currentUser)
                    .orElseThrow(() -> new RuntimeException("Current profile not found"));

            // ✅ Check if current profile follows the target
            isFollowing = currentProfile.getFollowing().contains(profile);
        }

        return new ProfileResponse(
                profile.getUser().getUsername(),
                profile.getBio(),
                profile.getImage(),
                isFollowing
        );
    }

    // ✅ Follow user
    @Override
    public ProfileResponse followUser(String username) {
        User currentUser = getCurrentUser();
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Profile currentProfile = profileRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Current profile not found"));
        Profile targetProfile = profileRepository.findByUser(targetUser)
                .orElseThrow(() -> new RuntimeException("Target profile not found"));

        // Add follow
        currentProfile.getFollowing().add(targetProfile);
        targetProfile.getFollowers().add(currentProfile);

        profileRepository.save(currentProfile);
        profileRepository.save(targetProfile);

        ProfileDto dto = ProfileDto.builder()
                .id(targetProfile.getId())
                .bio(targetProfile.getBio())
                .image(targetProfile.getImage())
                .user(targetUser)
                .build();

        return profileMapper.toProfileResponse(dto, true);
    }

    // ✅ Unfollow user
    @Override
    public ProfileResponse unfollowUser(String username) {
        User currentUser = getCurrentUser();
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Profile currentProfile = profileRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Current profile not found"));
        Profile targetProfile = profileRepository.findByUser(targetUser)
                .orElseThrow(() -> new RuntimeException("Target profile not found"));

        // Remove follow
        currentProfile.getFollowing().remove(targetProfile);
        targetProfile.getFollowers().remove(currentProfile);

        profileRepository.save(currentProfile);
        profileRepository.save(targetProfile);

        ProfileDto dto = ProfileDto.builder()
                .id(targetProfile.getId())
                .bio(targetProfile.getBio())
                .image(targetProfile.getImage())
                .user(targetUser)
                .build();

        return profileMapper.toProfileResponse(dto, false);
    }
}
