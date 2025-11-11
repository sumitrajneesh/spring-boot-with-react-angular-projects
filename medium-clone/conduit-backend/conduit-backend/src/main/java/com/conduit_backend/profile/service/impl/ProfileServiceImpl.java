package com.conduit_backend.profile.service.impl;

import com.conduit_backend.profile.dto.ProfileDto;
import com.conduit_backend.profile.dto.ProfileResponse;
import com.conduit_backend.profile.mapper.ProfileMapper;
import com.conduit_backend.profile.respository.ProfileRepository;
import com.conduit_backend.profile.service.ProfileService;
import com.conduit_backend.user.entity.User;
import com.conduit_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileMapper profileMapper;

    // 🧠 Helper: Get currently logged-in user
    private User getCurrentUser() {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    @Override
    public ProfileResponse getProfileByUsername(String username) {
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfileDto profile = profileRepository.findByUser(targetUser)
                .orElseGet(() -> {
                    ProfileDto p = ProfileDto.builder()
                            .bio("")
                            .image(targetUser.getImage())
                            .user(targetUser)
                            .build();
                    return profileRepository.save(p);
                });

        User currentUser = getCurrentUser();
        boolean following = profile.getFollowers().contains(currentUser);

        return profileMapper.toProfileResponse(profile, following);
    }

    @Override
    public ProfileResponse followUser(String username) {
        User currentUser = getCurrentUser();
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfileDto profile = profileRepository.findByUser(targetUser)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.getFollowers().add(currentUser);
        profileRepository.save(profile);

        return profileMapper.toProfileResponse(profile, true);
    }

    @Override
    public ProfileResponse unfollowUser(String username) {
        User currentUser = getCurrentUser();
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfileDto profile = profileRepository.findByUser(targetUser)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.getFollowers().remove(currentUser);
        profileRepository.save(profile);

        return profileMapper.toProfileResponse(profile, false);
    }
}