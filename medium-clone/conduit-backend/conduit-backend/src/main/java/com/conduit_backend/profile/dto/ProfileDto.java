package com.conduit_backend.profile.dto;

import com.conduit_backend.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {
    private Long id;
    private String bio;
    private String image;
    private User user;
}