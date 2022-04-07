package com.shareit.utils;

import org.springframework.stereotype.Service;

@Service
public class EmailValidatorAdapter implements EmailValidator {
    @Override
    public boolean isValid(String email) {
//        return org.apache.commons.validator.routines.EmailValidator.getInstance()
//                .isValid(email);
        return false;
    }
}
