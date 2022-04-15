package com.shareit.service.registration;

import com.shareit.data.repository.registration.ConfirmationTokenRepository;
import com.shareit.domain.dto.registration.ConfirmationToken;
import com.shareit.domain.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    //TODO add tests
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
        //TODO send email confirmation
    }

    public Optional<ConfirmationToken> getConfirmationTokenByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }


    public String createToken(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();
        saveConfirmationToken(
                new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userEntity));
        return token;
    }
}
