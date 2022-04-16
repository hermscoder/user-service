package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.entity.UserEntity;
import com.shareit.domain.dto.CreateUser;
import com.shareit.domain.dto.User;
import com.shareit.domain.dto.UserCreated;
import com.shareit.domain.entity.UserState;
import com.shareit.domain.mapper.UserMapper;
import com.shareit.service.registration.ConfirmationTokenService;
import com.shareit.utils.commons.exception.InvalidParameterException;
import com.shareit.exception.UserNotFoundException;
import com.shareit.infrastructure.cryptography.Encrypter;
import com.shareit.utils.validator.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final Encrypter encrypter;
    private final ConfirmationTokenService confirmationTokenService;

    public UserService(UserRepository userRepository, EmailValidator emailValidator, Encrypter encrypter, ConfirmationTokenService confirmationTokenService) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
        this.encrypter = encrypter;
        this.confirmationTokenService = confirmationTokenService;
    }

    public User findById(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        return UserMapper.INSTANCE.toModel(userOptional.get());
    }

    public UserCreated signUpUser(CreateUser createUser) {
        if(!emailValidator.isValid(createUser.getEmail())) {
            throw new InvalidParameterException("email");
        }

        if(!createUser.getPassword().equals(createUser.getPasswordConfirmation())) {
            throw new InvalidParameterException("passwordConfirmation");
        }

        UserEntity userEntity = userRepository.save(
                new UserEntity(createUser.getEmail(),
                        encrypter.encrypt(createUser.getPassword()),
                        createUser.getName(),
                        createUser.getBirthDate()));

        //TODO send email to the user to confirm the account
        confirmationTokenService.createAndsendEmailConfirmationTokenEmailToUser(userEntity);
        return new UserCreated(userEntity.getId(), "");
    }



    public int enableAppUser(String email) {
        return userRepository.changeUserState(UserState.CONFIRMED, email);
    }
}
