package com.shareit.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorAdapterTest {

    private final EmailValidatorAdapter emailValidatorAdapter;

    public EmailValidatorAdapterTest() {
        this.emailValidatorAdapter = new EmailValidatorAdapter();
    }
    @Test
    public void testIsValid() {
        boolean emailValid = emailValidatorAdapter.isValid("any_email@mail.com");
        assertFalse(emailValid);
    }

}