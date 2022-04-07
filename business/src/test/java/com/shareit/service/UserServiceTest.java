package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.User;
import com.shareit.utils.EmailValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailValidator emailValidator;

    private List<User> users = Collections.singletonList(
            new User(1L,
                    "any_email@mail.com",
                    "any_password",
                    "any_name",
                    LocalDate.now()));

    public UserServiceTest() {
        this.userService = new UserService(userRepository, emailValidator);
    }

    @Test
    public void testFindAll() {
        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.findAll();

        assertNotNull(allUsers);
        assertEquals(allUsers.size(), users.size());
        assertEquals(allUsers.get(0), users.get(0));
    }



}