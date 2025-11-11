package com.conduit_backend.config;


import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class JwtTokenBlacklist {

    private final Set<String> blacklistedTokens = new HashSet<>();

    public void add(String token) {
        blacklistedTokens.add(token);
    }

    public boolean contains(String token) {
        return blacklistedTokens.contains(token);
    }

    public void remove(String token) {
        blacklistedTokens.remove(token);
    }
}
