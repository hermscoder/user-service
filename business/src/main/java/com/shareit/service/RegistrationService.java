package com.shareit.service;

import com.shareit.domain.dto.CreateUser;
import com.shareit.domain.dto.UserCreated;
import com.shareit.domain.dto.registration.ConfirmationToken;
import com.shareit.domain.entity.UserEntity;
import com.shareit.service.registration.ConfirmationTokenService;
import com.shareit.utils.commons.exception.BadRequestException;
import org.springframework.stereotype.Service;


@Service
public class RegistrationService {
    //TODO add tests
    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;

    public RegistrationService(ConfirmationTokenService confirmationTokenService, UserService userService) {
        this.confirmationTokenService = confirmationTokenService;
        this.userService = userService;
    }

    public UserCreated registerUser(CreateUser createUser) {
        UserEntity userEntity = userService.signUpUser(createUser);

        confirmationTokenService.createAndsendEmailConfirmationTokenEmailToUser(userEntity);
        return new UserCreated(userEntity.getId(), "");
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByToken(token)
                .orElseThrow(() -> new BadRequestException("token not found"));

        if(confirmationToken.getConfirmedAt() != null) {
            throw new BadRequestException("email already confirmed");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(confirmationToken.getUser().getEmail());

        return "confirmed";
    }
}
