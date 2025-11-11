package com.conduit_backend.config;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        var key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
    }
}
