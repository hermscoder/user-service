package com.shareit.presentation;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.User;
import com.shareit.domain.dto.CreateUser;
import com.shareit.domain.dto.UserCreated;
import com.shareit.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findAll();
    }

    @PostMapping
    public UserCreated userRegister(CreateUser createUser) {
        return new UserCreated(userService.createUser(createUser));
    }
}
