package com.shareit.presentation;

import com.shareit.service.UserService;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final UserController userController;

    @Mock
    private UserService userService;

    public UserControllerTest() {
        userController = new UserController(userService);
    }
}