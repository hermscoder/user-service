package com.shareit.infrastructure.cryptography;

public interface Encrypter {
    String encrypt(String value);
    String decrypt(String value);
}
