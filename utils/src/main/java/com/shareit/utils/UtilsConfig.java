package com.shareit.utils;

import com.shareit.utils.commons.provider.DateProvider;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {

    @Bean
    public EmailValidator getEmailValidator() {
        return EmailValidator.getInstance();
    }

    @Bean
    public DateProvider getDateProvider() {
        return new DateProvider();
    }
}
