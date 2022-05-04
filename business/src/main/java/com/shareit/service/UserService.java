package com.shareit.service;

import com.shareit.data.repository.UserRepository;
import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.UserEntity;
import com.shareit.domain.dto.registration.UserRegistration;
import com.shareit.domain.dto.User;
import com.shareit.domain.entity.UserState;
import com.shareit.domain.mapper.UserMapper;
import com.shareit.utils.commons.exception.InvalidParameterException;
import com.shareit.exception.UserNotFoundException;
import com.shareit.infrastructure.cryptography.Encrypter;
import com.shareit.utils.validator.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final Encrypter encrypter;
    private final WebClient webClient;

    public UserService(UserRepository userRepository, EmailValidator emailValidator, Encrypter encrypter, WebClient webClient) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
        this.encrypter = encrypter;
        this.webClient = webClient;
    }

    public User getUserById(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(userId);
        }

        Media userMedia = webClient.get()
                .uri("http://localhost:8082/v1/media",
                        uriBuilder -> uriBuilder.build(1))
                .retrieve()
                .bodyToMono(Media.class)
                .block();

        User user = UserMapper.INSTANCE.toModel(userOptional.get());


        return user;
    }

    public UserEntity signUpUser(UserRegistration userRegistration) {
        if(!emailValidator.isValid(userRegistration.getEmail())) {
            throw new InvalidParameterException("email");
        }

        if(!userRegistration.getPassword().equals(userRegistration.getPasswordConfirmation())) {
            throw new InvalidParameterException("passwordConfirmation");
        }

        UserEntity userEntity = userRepository.save(
                new UserEntity(userRegistration.getEmail(),
                        encrypter.encrypt(userRegistration.getPassword()),
                        userRegistration.getName(),
                        userRegistration.getBirthDate()));
        return userEntity;
    }



    public int enableAppUser(String email) {
        return userRepository.changeUserState(UserState.CONFIRMED, email);
    }
}
