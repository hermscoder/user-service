package com.shareit.infrastructure.cryptography;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptAdapter implements Encrypter {

    public final PasswordEncoder passwordEncoder;

    public BCryptAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encrypt(String value) {
        return passwordEncoder.encode(value);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
