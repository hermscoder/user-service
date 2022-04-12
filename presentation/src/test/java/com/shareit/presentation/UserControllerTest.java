package com.shareit.presentation;

import com.shareit.domain.dto.CreateUser;
import com.shareit.domain.dto.User;
import com.shareit.domain.dto.UserCreated;
import com.shareit.domain.entity.UserState;
import com.shareit.exception.UserNotFoundException;
import com.shareit.service.UserService;
import com.shareit.utils.commons.exception.InvalidParameterException;
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
        expectedUser = new User(1L, "test@gmail.com", "password", "test", LocalDate.now(), true);
    }

    @Test
    public void testGetUserById() {
        when(userService.findById(anyLong())).thenReturn(expectedUser);
        ResponseEntity<User> userByIdResponseEntity = userController.getUserById(1L);
        assertNotNull(userByIdResponseEntity);
        assertEquals(HttpStatus.OK, userByIdResponseEntity.getStatusCode());
        assertNotNull(userByIdResponseEntity.getBody());
        assertEquals(expectedUser, userByIdResponseEntity.getBody());
    }

    @Test
    public void testGetUserByIdThrowUserNotFoundException() {
        when(userService.findById(anyLong())).thenThrow(new UserNotFoundException(1L));

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userController.getUserById(1L));

        assertNotNull(userNotFoundException);
        assertEquals(1L, userNotFoundException.getUserId());
        assertEquals("User not found: 1", userNotFoundException.getMessage());
    }

    @Test
    public void testUserRegister() {
        when(userService.createUser(any(CreateUser.class))).thenReturn(new UserCreated(1L));

        ResponseEntity<UserCreated> userCreatedResponseEntity = userController.userRegister(
                new CreateUser("any_email@mail.com",
                        "any_password",
                        "any_password",
                        "any_name",
                        LocalDate.now()));
        assertNotNull(userCreatedResponseEntity);
        assertEquals(HttpStatus.OK, userCreatedResponseEntity.getStatusCode());
        assertEquals(1L, userCreatedResponseEntity.getBody().getId());
    }

    @Test
    public void testUserRegisterThrowInvalidParameterException() {
        when(userService.createUser(any(CreateUser.class))).thenThrow(new InvalidParameterException("passwordConfirmation"));

        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> userController.userRegister(
                new CreateUser("any_email@mail.com",
                        "any_password",
                        "wrong_password",
                        "any_name",
                        LocalDate.now())));

        assertNotNull(invalidParameterException);
        assertEquals("passwordConfirmation", invalidParameterException.getParamName());
        assertEquals("Invalid param: passwordConfirmation", invalidParameterException.getMessage());
    }

}