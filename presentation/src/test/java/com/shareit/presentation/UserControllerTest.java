package com.shareit.presentation;

import com.shareit.domain.dto.User;
import com.shareit.exception.UserNotFoundException;
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
        expectedUser = new User(1L, "test", LocalDate.now(), true);
    }

    @Test
    public void testGetUserById() {
        when(userService.getUserById(anyLong())).thenReturn(expectedUser);
        ResponseEntity<User> userByIdResponseEntity = userController.getUserById(1L);
        assertNotNull(userByIdResponseEntity);
        assertEquals(HttpStatus.OK, userByIdResponseEntity.getStatusCode());
        assertNotNull(userByIdResponseEntity.getBody());
        assertEquals(expectedUser, userByIdResponseEntity.getBody());
    }

    @Test
    public void testGetUserByIdThrowUserNotFoundException() {
        when(userService.getUserById(anyLong())).thenThrow(new UserNotFoundException(1L));

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userController.getUserById(1L));

        assertNotNull(userNotFoundException);
        assertEquals(1L, userNotFoundException.getUserId());
        assertEquals("User not found: 1", userNotFoundException.getMessage());
    }


}