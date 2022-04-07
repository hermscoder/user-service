package com.shareit.utils;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Configurable
public class UtilsConfig {

    @Bean
    public EmailValidator getEmailValidator() {
        return EmailValidator.getInstance();
    }
}
