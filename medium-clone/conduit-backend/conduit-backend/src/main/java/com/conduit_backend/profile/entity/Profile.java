package com.conduit_backend.profile.entity;

import com.conduit_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bio;
    private String image;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Users that follow this profile
    @ManyToMany
    @JoinTable(
            name = "profile_followers",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<Profile> followers = new HashSet<>();

    // Users this profile follows
    @ManyToMany(mappedBy = "followers")
    private Set<Profile> following = new HashSet<>();
}
