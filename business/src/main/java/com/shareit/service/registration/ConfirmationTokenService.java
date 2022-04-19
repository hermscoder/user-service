package com.shareit.service.registration;

import com.shareit.data.repository.registration.ConfirmationTokenRepository;
import com.shareit.domain.dto.email.ConfirmationEmailData;
import com.shareit.domain.dto.registration.ConfirmationTokenEntity;
import com.shareit.domain.entity.UserEntity;
import com.shareit.utils.commons.provider.DateProvider;
import com.shareit.utils.commons.email.EmailSender;
import com.shareit.utils.commons.email.MailDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ConfirmationTokenService.class);

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSender emailSender;
    private final DateProvider dateProvider;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, EmailSender emailSender, DateProvider dateProvider) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSender = emailSender;
        this.dateProvider = dateProvider;
    }

    public void saveConfirmationToken(ConfirmationTokenEntity token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationTokenEntity> getConfirmationTokenByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, dateProvider.getNowDateTime());
    }


    String createToken(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();
        saveConfirmationToken(
                new ConfirmationTokenEntity(token, dateProvider.getNowDateTime(), dateProvider.getNowDateTime().plusMinutes(15), userEntity));
        return token;
    }

    public void createAndSendEmailConfirmationTokenEmailToUser(UserEntity userEntity) {
        String token = createToken(userEntity);
        //TODO replace for website URL
        String confirmationLink = "http://localhost:8080/v1/registration/confirm?token=" + token;


        emailSender.send(MailDetail.newBuilder()
                        .from("shareit")
                        .to(userEntity.getEmail())
                        .name(userEntity.getName())
                        .subject("Confirm your email")
                        .build(),
                ConfirmationEmailData.builder()
                        .name(userEntity.getName())
                        .link(confirmationLink).build())
                .whenCompleteAsync((res, ex) -> {
                    if (ex != null) {
                        LOGGER.error(ex.getMessage(), ex);
                        //TODO add logic to email send retry
                    }
                });
    }
}
