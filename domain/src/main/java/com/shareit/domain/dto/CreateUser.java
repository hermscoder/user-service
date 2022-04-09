package com.shareit.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shareit.domain.validation.NotEmpty;
import com.shareit.utils.commons.deserializer.LocalDateDeserializer;
import com.shareit.utils.commons.serializer.LocalDateSerializer;

import java.time.LocalDate;


public class CreateUser {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String passwordConfirmation;
    @NotEmpty
    private String name;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthDate;

    public CreateUser() {
    }

    public CreateUser(String email, String password, String passwordConfirmation, String name, LocalDate birthDate) {
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}
