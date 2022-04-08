package com.shareit.presentation;

import com.shareit.domain.User;
import com.shareit.domain.dto.CreateUser;
import com.shareit.domain.dto.UserCreated;
import com.shareit.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


public class UserControllerTest {

    private final UserController userController;
    private final UserService userService;

    private final List<User> userList;

    public UserControllerTest() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
        User expected = new User(1L, "test@gmail.com", "password", "test", LocalDate.now());
        userList = Collections.singletonList(expected);
    }

    @Test
    public void testGetUserById() {

        when(userService.findById(anyLong())).thenReturn(userList.get(0));
        User user = userController.getUserById(1L);
        assertNotNull(user);
        assertEquals(userList.get(0), user);
    }

    @Test
    public void testUserRegister() {
        when(userService.createUser(any(CreateUser.class))).thenReturn(1L);

        UserCreated userCreated = userController.userRegister(
                new CreateUser("any_email@mail.com",
                        "any_password",
                        "any_password",
                        "any_name",
                        LocalDate.now()));
        assertNotNull(userCreated);
        assertEquals(1L, userCreated.getId());
    }
}