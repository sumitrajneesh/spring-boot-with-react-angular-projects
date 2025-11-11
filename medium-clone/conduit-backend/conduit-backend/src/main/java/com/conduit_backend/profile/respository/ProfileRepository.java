package com.conduit_backend.profile.respository;

import com.conduit_backend.profile.dto.ProfileDto;
import com.conduit_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileDto, Long> {
    Optional<ProfileDto> findByUser(User user);
}
