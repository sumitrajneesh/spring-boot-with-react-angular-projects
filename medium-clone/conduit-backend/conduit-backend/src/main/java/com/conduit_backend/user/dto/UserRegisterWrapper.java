package com.conduit_backend.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserRegisterWrapper {

    @Valid
    @NotNull
    private UserRegisterRequest user;
}
