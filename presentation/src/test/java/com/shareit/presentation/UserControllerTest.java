package com.shareit.presentation;

import com.shareit.domain.dto.CreateUser;
import com.shareit.domain.dto.User;
import com.shareit.domain.dto.UserCreated;
import com.shareit.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


public class UserControllerTest {

    private final UserController userController;
    private final UserService userService;

    private final User expectedUser;

    public UserControllerTest() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
        expectedUser = new User(1L, "test@gmail.com", "password", "test", LocalDate.now());
    }

    @Test
    public void testGetUserById() {

        when(userService.findById(anyLong())).thenReturn(expectedUser);
        ResponseEntity<User> userById = userController.getUserById(1L);
        assertNotNull(userById);
        assertEquals(HttpStatus.OK, userById.getStatusCode());
        assertEquals(expectedUser, userById.getBody());
    }

    @Test
    public void testUserRegister() {
        when(userService.createUser(any(CreateUser.class))).thenReturn(1L);

        ResponseEntity<UserCreated> userCreated = userController.userRegister(
                new CreateUser("any_email@mail.com",
                        "any_password",
                        "any_password",
                        "any_name",
                        LocalDate.now()));
        assertNotNull(userCreated);
        assertEquals(HttpStatus.OK, userCreated.getStatusCode());
        assertEquals(1L, userCreated.getBody().getId());
    }
}