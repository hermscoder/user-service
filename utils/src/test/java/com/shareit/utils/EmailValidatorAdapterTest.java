package com.shareit.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorAdapterTest {

    private final EmailValidatorAdapter emailValidatorAdapter;

    public EmailValidatorAdapterTest() {
        this.emailValidatorAdapter = new EmailValidatorAdapter();
    }

    public void testIsValid() {
        boolean emailValid = emailValidatorAdapter.isValid("any_email@mail.com");
        assertTrue(emailValid);
    }
}