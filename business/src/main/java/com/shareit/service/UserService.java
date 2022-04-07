package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.User;
import com.shareit.domain.dto.CreateUser;
import com.shareit.utils.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;

    public UserService(UserRepository userRepository, EmailValidator emailValidator) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Long createUser(CreateUser createUser) {
        if(emailValidator.isValid(createUser.getEmail())) {
            //throw error
        }
        User user = userRepository.save(
                new User(createUser.getEmail(),
                        createUser.getPassword(),
                        createUser.getName(),
                        createUser.getBirthDate()));
        return user.getId();
    }
}
