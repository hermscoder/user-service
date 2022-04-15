package com.shareit.service;

import com.shareit.domain.dto.registration.ConfirmationToken;
import com.shareit.service.registration.ConfirmationTokenService;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    //TODO add tests
    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;

    public AuthService(ConfirmationTokenService confirmationTokenService, UserService userService) {
        this.confirmationTokenService = confirmationTokenService;
        this.userService = userService;
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(confirmationToken.getUser().getEmail());

        return "confirmed";
    }
}
