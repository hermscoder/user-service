package com.shareit.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shareit.domain.dto.Media;
import com.shareit.domain.dto.registration.UserRegistration;
import com.shareit.service.client.MediaClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserAndRegistrationControllersWebTest {

    private static final String REGISTRATION_ENDPOINT = "/v1/registration";
    private static final String USER_ENDPOINT = "/v1/user";

    private final LocalDate birthDate = LocalDate.now();
    private final UserRegistration userRegistration = new UserRegistration(
            "any_email@mail.com",
            "any_password",
            "any_password",
            "any_name",
            birthDate);

    @MockBean
    private MediaClient mediaClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testUserRegisterOk() throws Exception {

        mockMvc.perform(
                post(REGISTRATION_ENDPOINT)
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
                post(REGISTRATION_ENDPOINT)
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
                post(REGISTRATION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRegistration))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing param: name"));
    }

    @Test
    @Order(4)
    public void testGetUserById() throws Exception {
        when(mediaClient.getMediaById(anyLong())).thenReturn(Media.builder().id(1L).type("IMAGE").url("http://test.url.com").build());

        mockMvc.perform(get(USER_ENDPOINT + "/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.birthDate").value(birthDate.toString()))
                .andExpect(jsonPath("$.confirmed").value("false"))
                .andExpect(jsonPath("$.name").value("any_name"));
    }

    @Test
    @Order(5)
    public void testEmailConfirmationWithoutToken() throws Exception {
        mockMvc.perform(
                get(REGISTRATION_ENDPOINT + "/confirm?token=").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BadRequestException"))
                .andExpect(jsonPath("$.message").value("token not found"));
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}