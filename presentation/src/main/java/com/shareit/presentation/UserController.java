package com.shareit.presentation;

import com.shareit.domain.dto.CreateUser;
import com.shareit.domain.dto.User;
import com.shareit.domain.dto.UserCreated;
import com.shareit.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/v1/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long userId) {
        //TODO Return ResponseEntity or create a exception handler to handle different type of exceptions
        return userService.findById(userId);
    }

    @PostMapping
    public UserCreated userRegister(@Valid @RequestBody CreateUser createUser) {
        return new UserCreated(userService.createUser(createUser));
    }
}
