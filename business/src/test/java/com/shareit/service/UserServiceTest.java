package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.UserEntity;
import com.shareit.domain.dto.CreateUser;
import com.shareit.domain.dto.User;
import com.shareit.domain.dto.UserCreated;
import com.shareit.utils.commons.exception.InvalidParameterException;
import com.shareit.exception.UserNotFoundException;
import com.shareit.infrastructure.cryptography.Encrypter;
import com.shareit.utils.validator.EmailValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
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

    private final LocalDate birthDate = LocalDate.now();
    private final CreateUser createUser = new CreateUser(
            "any_email@mail.com",
            "any_password",
            "any_password",
            "any_name",
            birthDate);

    private UserEntity userEntities = new UserEntity(1L,
            "any_email@mail.com",
            "any_password",
            "any_name",
            birthDate);

    private User userModelExpected = new User(1L,
            "any_email@mail.com",
            "any_password",
            "any_name",
            birthDate);

    public UserServiceTest() {
        userRepository = Mockito.mock(UserRepository.class);
        emailValidator = Mockito.mock(EmailValidator.class);
        encrypter = Mockito.mock(Encrypter.class);

        this.userService = new UserService(userRepository, emailValidator, encrypter);
    }

    @Test
    public void testFindById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntities));

        User user = userService.findById(1L);

        assertNotNull(user);
        assertEquals(userModelExpected, user);
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
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntities);
        createUser.setPasswordConfirmation("wrong_password");

        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> userService.createUser(createUser));
        assertEquals("passwordConfirmation", invalidParameterException.getParamName());
        assertEquals("Invalid param: passwordConfirmation", invalidParameterException.getMessage());
    }

    @Test
    public void testCreateUserWhenEncrypterThrowsException() {
        when(emailValidator.isValid(anyString())).thenReturn(true);
        when(encrypter.encrypt(anyString())).thenThrow(RuntimeException.class);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntities);

        assertThrows(Exception.class, () -> userService.createUser(createUser));
    }

    @Test
    public void testCreateUser() {
        when(emailValidator.isValid(anyString())).thenReturn(true);
        when(encrypter.encrypt(anyString())).thenReturn("HHV$%%^5478yhgvbtFv34#$b");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntities);


        UserCreated userCreated = userService.createUser(createUser);

        verify(userRepository).save(
                new UserEntity(createUser.getEmail(),
                "HHV$%%^5478yhgvbtFv34#$b",
                        createUser.getName(),
                        createUser.getBirthDate()));
        assertEquals(1L, userCreated.getId());
    }

}