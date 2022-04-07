package com.shareit.utils;

import org.springframework.stereotype.Service;

@Service
public class EmailValidatorAdapter implements EmailValidator {

    private final org.apache.commons.validator.routines.EmailValidator emailValidator;

    public EmailValidatorAdapter(org.apache.commons.validator.routines.EmailValidator emailValidator) {
        this.emailValidator = emailValidator;
    }

    @Override
    public boolean isValid(String email) {
        return emailValidator.isValid(email);
    }
}
