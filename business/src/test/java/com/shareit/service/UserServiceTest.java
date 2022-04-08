package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.User;
import com.shareit.domain.dto.CreateUser;
import com.shareit.exception.InvalidParameterException;
import com.shareit.exception.UserNotFoundException;
import com.shareit.infrastructure.cryptography.Encrypter;
import com.shareit.utils.EmailValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    public void testFindById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(users.get(0)));

        User user = userService.findById(1L);

        assertNotNull(user);
        assertEquals(users.get(0), user);
    }

    @Test
    public void testFindByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userService.findById(1L));

        assertEquals(1L, userNotFoundException.getUserId());
        assertEquals("User not found: 1", userNotFoundException.getMessage());
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
        assertEquals("Invalid param: passwordConfirmation", invalidParameterException.getMessage());
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
        when(encrypter.encrypt(anyString())).thenReturn("HHV$%%^5478yhgvbtFv34#$b");
        when(userRepository.save(any(User.class))).thenReturn(users.get(0));


        Long userId = userService.createUser(createUser);

        verify(userRepository).save(
                new User(createUser.getEmail(),
                "HHV$%%^5478yhgvbtFv34#$b",
                        createUser.getName(),
                        createUser.getBirthDate()));
        assertEquals(1L, userId);
    }

}