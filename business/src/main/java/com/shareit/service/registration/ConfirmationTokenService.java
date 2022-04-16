package com.shareit.service.registration;

import com.shareit.data.repository.registration.ConfirmationTokenRepository;
import com.shareit.domain.dto.registration.ConfirmationToken;
import com.shareit.domain.entity.UserEntity;
import com.shareit.utils.commons.email.EmailSender;
import com.shareit.utils.commons.email.MailDetail;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSender emailSender;
    //TODO add tests
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, EmailSender emailSender) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSender = emailSender;
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getConfirmationTokenByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }


    private String createToken(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();
        saveConfirmationToken(
                new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userEntity));
        return token;
    }

    public void createAndsendEmailConfirmationTokenEmailToUser(UserEntity userEntity) {
        String token = createToken(userEntity);
        //TODO replace for website URL
        String confirmationLink = "http://localhost:8080/v1/registration/confirm?token=" + token;

        HashMap<String, Object> templateValues = new HashMap<>();
        templateValues.put("name", userEntity.getName());
        templateValues.put("link", confirmationLink);

        emailSender.send(MailDetail.newBuilder()
                        .from("shareit")
                        .to(userEntity.getEmail())
                        .name(userEntity.getName())
                        .subject("Confirm your email")
                        .build(), templateValues);
    }
}
