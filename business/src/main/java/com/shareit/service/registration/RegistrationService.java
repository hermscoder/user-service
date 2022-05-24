package com.shareit.service.registration;

import com.shareit.domain.dto.registration.UserRegistration;
import com.shareit.domain.dto.registration.UserCreated;
import com.shareit.domain.dto.registration.ConfirmationTokenEntity;
import com.shareit.domain.entity.UserEntity;
import com.shareit.service.UserService;
import com.shareit.utils.commons.exception.BadRequestException;
import org.springframework.stereotype.Service;


@Service
public class RegistrationService {

    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;

    public RegistrationService(ConfirmationTokenService confirmationTokenService, UserService userService) {
        this.confirmationTokenService = confirmationTokenService;
        this.userService = userService;
    }

    public UserCreated registerUser(UserRegistration userRegistration) {
        UserEntity userEntity = userService.signUpUser(userRegistration);

        confirmationTokenService.createAndSendEmailConfirmationTokenEmailToUser(userEntity);
        return new UserCreated(userEntity.getId());
    }

    public String confirmToken(String token) {
        ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenService.getConfirmationTokenByToken(token)
                .orElseThrow(() -> new BadRequestException("token not found"));

        if(confirmationTokenEntity.getConfirmedAt() != null) {
            throw new BadRequestException("email already confirmed");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(confirmationTokenEntity.getUser().getId());

        return "confirmed";
    }
}
