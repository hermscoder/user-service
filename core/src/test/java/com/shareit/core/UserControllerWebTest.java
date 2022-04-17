package com.shareit.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shareit.domain.dto.UserRegistration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerWebTest {

    private static final String USER_ENDPOINT = "/v1/user";

    @Autowired
    private MockMvc mockMvc;

    private final LocalDate birthDate = LocalDate.now();
    private final UserRegistration userRegistration = new UserRegistration(
            "any_email@mail.com",
            "any_password",
            "any_password",
            "any_name",
            birthDate);

    @Test
    @Order(1)
    public void testUserRegisterOk() throws Exception {

        mockMvc.perform(
                post(USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRegistration))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andReturn();
    }

    @Test
    @Order(2)
    public void testUserRegisterEmailNotValid() throws Exception {
        UserRegistration userRegistration = new UserRegistration(
                "invalid_email@@.com",
                "any_password",
                "any_password",
                "any_name",
                LocalDate.now());

        mockMvc.perform(
                post(USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRegistration))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid param: email"));
    }

    @Test
    @Order(3)
    public void testUserRegisterWithoutName() throws Exception {
        UserRegistration userRegistration = new UserRegistration(
                "invalid_email@.com",
                "any_password",
                "any_password",
                null,
                LocalDate.now());

        mockMvc.perform(
                post(USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRegistration))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing param: name"));
    }

    @Test
    @Order(4)
    public void testGetUserById() throws Exception {
        mockMvc.perform(get(USER_ENDPOINT + "/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.birthDate").value(birthDate.toString()))
                .andExpect(jsonPath("$.email").value("any_email@mail.com"))
                .andExpect(jsonPath("$.name").value("any_name"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}