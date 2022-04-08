package com.shareit.infrastructure.cryptography;

public interface Encrypter {
    String encrypt(String value);
    boolean matches(String rawPassword, String encodedPassword);
}
