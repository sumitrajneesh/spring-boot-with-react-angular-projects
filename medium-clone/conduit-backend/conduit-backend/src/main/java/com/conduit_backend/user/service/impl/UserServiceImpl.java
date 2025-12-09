package com.conduit_backend.user.service.impl;

import com.conduit_backend.config.JwtTokenBlacklist;
import com.conduit_backend.config.JwtTokenProvider;
import com.conduit_backend.user.dto.*;
import com.conduit_backend.user.entity.User;
import com.conduit_backend.user.mapper.UserMapper;
import com.conduit_backend.user.repository.UserRepository;
import com.conduit_backend.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenBlacklist jwtTokenBlacklist;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    @Transactional
    public UserResponse register(UserRegisterWrapper request) {

        log.info("Starting user registration for email={}", request.getUser().getEmail());

        UserRegisterRequest user = request.getUser(); // extract inner object

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User newUser = User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .bio("")
                .image("https://raw.githubusercontent.com/gothinkster/node-express-realworld-example-app/refs/heads/master/src/assets/images/smiley-cyrus.jpeg")
                .build();

        User saved = userRepository.save(newUser);
        String token = jwtTokenProvider.generateToken(saved.getUsername());

        return userMapper.toUserResponse(saved, token);
    }



    @Override
    @Transactional
    public UserResponse login(UserLoginWrapper request) {

        log.info("Starting user  for email={}", request.getUser().getEmail());

        UserLoginRequest user = request.getUser();

        User entity = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if(!passwordEncoder.matches(user.getPassword(), entity.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(entity.getUsername());
        return userMapper.toUserResponse(entity, token);
    }

    @Override
    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtTokenProvider.generateToken(user.getUsername());
        return userMapper.toUserResponse(user, token);
    }

    @Override
    @Transactional
    public UserResponse updateUser(String username, UpdateUserRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getEmail() != null && !request.getEmail().isBlank()) user.setEmail(request.getEmail());
        if (request.getUsername() != null && !request.getUsername().isBlank()) user.setUsername(request.getUsername());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getImage() != null) user.setImage(request.getImage());
        if (request.getPassword() != null && !request.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(saved.getUsername());
        return userMapper.toUserResponse(saved, token);
    }

    @Transactional
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtTokenProvider.getUsernameFromToken(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);

        jwtTokenBlacklist.add(token);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Transactional
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        jwtTokenBlacklist.add(token); // Add token to blacklist
        return ResponseEntity.ok("Logged out successfully");
    }

    public boolean isTokenBlacklisted(String token) {
        return jwtTokenBlacklist.contains(token);
    }
}
