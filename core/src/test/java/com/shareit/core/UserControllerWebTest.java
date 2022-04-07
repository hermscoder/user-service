package com.shareit.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shareit.domain.dto.CreateUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerWebTest {

    private static final String USER_ENDPOINT = "/v1/user";
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetUsers() throws Exception {
        mockMvc.perform(get(USER_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].birthDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$[0].name").value("test"))
                .andExpect(jsonPath("$[0].password").value("password"));
    }

    @Test
    public void testUserRegisterOk() throws Exception {
        CreateUser createUser = new CreateUser(
                "any_email@mail.com",
                "any_password",
                "any_password",
                "any_name",
                LocalDate.now());

        mockMvc.perform(
                post(USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andReturn();
    }

    @Test
    public void testUserRegisterEmailNotValid() throws Exception {
        CreateUser createUser = new CreateUser(
                "invalid_email@mail.com",
                "any_password",
                "any_password",
                "any_name",
                LocalDate.now());

        mockMvc.perform(
                post(USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(1L))
                .andReturn();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}