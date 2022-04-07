package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.User;
import com.shareit.domain.dto.CreateUser;
import com.shareit.exception.InvalidParameterException;
import com.shareit.infrastructure.cryptography.Encrypter;
import com.shareit.utils.EmailValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserService userService;

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final Encrypter encrypter;

    private final CreateUser createUser = new CreateUser(
            "any_email@mail.com",
            "any_password",
            "any_password",
            "any_name",
            LocalDate.now());

    private List<User> users = Collections.singletonList(
            new User(1L,
                    "any_email@mail.com",
                    "any_password",
                    "any_name",
                    LocalDate.now()));

    public UserServiceTest() {
        userRepository = Mockito.mock(UserRepository.class);
        emailValidator = Mockito.mock(EmailValidator.class);
        encrypter = Mockito.mock(Encrypter.class);

        this.userService = new UserService(userRepository, emailValidator, encrypter);
    }

    @Test
    public void testFindAll() {
        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.findAll();

        assertNotNull(allUsers);
        assertEquals(allUsers.size(), users.size());
        assertEquals(allUsers.get(0), users.get(0));
    }

    @Test
    public void testCreateUserWhenInvalidEmailProvided() {
        when(emailValidator.isValid(anyString())).thenReturn(false);
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> userService.createUser(createUser));
        assertEquals("email", invalidParameterException.getParamName());
    }

    @Test
    public void testCreateUserWithWrongPasswordConfirmation() {
        when(emailValidator.isValid(anyString())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(users.get(0));
        createUser.setPasswordConfirmation("wrong_password");

        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> userService.createUser(createUser));
        assertEquals("passwordConfirmation", invalidParameterException.getParamName());
    }

    @Test
    public void testCreateUserWhenEncrypterThrowsException() {
        when(emailValidator.isValid(anyString())).thenReturn(true);
        when(encrypter.encrypt(anyString())).thenThrow(RuntimeException.class);
        when(userRepository.save(any(User.class))).thenReturn(users.get(0));

        assertThrows(Exception.class, () -> userService.createUser(createUser));
    }

    @Test
    public void testCreateUser() {
        when(emailValidator.isValid(anyString())).thenReturn(true);
        when(encrypter.encrypt(anyString())).thenReturn("encrypted_password");
        when(userRepository.save(any(User.class))).thenReturn(users.get(0));


        Long userId = userService.createUser(createUser);

        verify(userRepository).save(
                new User(createUser.getEmail(),
                "encrypted_password",
                        createUser.getName(),
                        createUser.getBirthDate()));
        assertEquals(1L, userId);
    }

}