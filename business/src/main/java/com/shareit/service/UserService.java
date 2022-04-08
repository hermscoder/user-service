package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.User;
import com.shareit.domain.dto.CreateUser;
import com.shareit.exception.InvalidParameterException;
import com.shareit.exception.UserNotFoundException;
import com.shareit.infrastructure.cryptography.Encrypter;
import com.shareit.utils.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final Encrypter encrypter;

    public UserService(UserRepository userRepository, EmailValidator emailValidator, Encrypter encrypter) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
        this.encrypter = encrypter;
    }

    public User findById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        return userOptional.get();
    }

    public Long createUser(CreateUser createUser) {
        if(!emailValidator.isValid(createUser.getEmail())) {
            throw new InvalidParameterException("email");
        }

        if(!createUser.getPassword().equals(createUser.getPasswordConfirmation())) {
            throw new InvalidParameterException("passwordConfirmation");
        }

        User user = userRepository.save(
                new User(createUser.getEmail(),
                        encrypter.encrypt(createUser.getPassword()),
                        createUser.getName(),
                        createUser.getBirthDate()));
        return user.getId();
    }
}
