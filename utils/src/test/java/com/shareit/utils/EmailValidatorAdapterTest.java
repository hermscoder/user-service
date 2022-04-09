package com.shareit.utils;

import com.shareit.utils.validator.EmailValidatorAdapter;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorAdapterTest {

    private final EmailValidatorAdapter emailValidatorAdapter;
    private EmailValidator emailValidator;

    public EmailValidatorAdapterTest() {
        emailValidator = Mockito.mock(EmailValidator.class);
        this.emailValidatorAdapter = new EmailValidatorAdapter(emailValidator);
    }

    @Test
    public void testIsValid() {
        Mockito.when(emailValidator.isValid(ArgumentMatchers.any())).thenReturn(true);
        boolean emailValid = emailValidatorAdapter.isValid("any_email@mail.com");
        assertTrue(emailValid);
    }

    @Test
    public void testIsNotValid() {
        Mockito.when(emailValidator.isValid(ArgumentMatchers.any())).thenReturn(false);
        boolean emailValid = emailValidatorAdapter.isValid("any_email@mail.com");
        assertFalse(emailValid);
    }

}