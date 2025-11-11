package com.conduit_backend.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    // stored as BCrypt hash
    @Column(nullable = false)
    private String password;

    @Column(length = 512)
    private String bio;

    private String image;

    @Builder.Default
    private Boolean following = false;
}
