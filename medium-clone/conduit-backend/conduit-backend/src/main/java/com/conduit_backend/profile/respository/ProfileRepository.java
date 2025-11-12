package com.conduit_backend.profile.respository;

import com.conduit_backend.profile.entity.Profile;
import com.conduit_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);

}
