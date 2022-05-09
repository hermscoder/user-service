package com.shareit.service.registration;

import com.shareit.data.repository.registration.ConfirmationTokenRepository;
import com.shareit.domain.dto.email.ConfirmationEmailData;
import com.shareit.domain.dto.registration.ConfirmationTokenEntity;
import com.shareit.domain.entity.UserEntity;
import com.shareit.domain.entity.UserState;
import com.shareit.utils.commons.email.EmailDataModel;
import com.shareit.utils.commons.email.MailDetail;
import com.shareit.utils.commons.exception.EmailSenderException;
import com.shareit.utils.commons.provider.DateProvider;
import com.shareit.utils.commons.email.EmailSender;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConfirmationTokenServiceTest {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;

    private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJlbWFpbCI6ImF5bGFuQGJvc2Nhcmluby5" +
            "jb20iLCJwYXNzd29yZCI6InlhMGdzcWh5NHd6dnV2YjQifQ.yN_8-" +
            "Mge9mFgsnYHnPEh_ZzNP7YKvSbQ3Alug9HMCsM";
    private UserEntity userEntity = new UserEntity(1L,
            "any_email@mail.com",
            "HHV$%%^5478yhgvbtFv34#$b",
            "any_name",
            LocalDate.now().minusYears(10L),
            UserState.CONFIRMED,
            1L);
    private ConfirmationTokenEntity expectedConfirmationTokenEntity = new ConfirmationTokenEntity(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15L),
            userEntity);

    private final LocalDateTime dateTime = LocalDateTime.now();

    ConfirmationTokenServiceTest() {
        this.confirmationTokenRepository = Mockito.mock(ConfirmationTokenRepository.class);
        this.emailSender = Mockito.mock(EmailSender.class);

        this.confirmationTokenService =
                new ConfirmationTokenService(
                        confirmationTokenRepository,
                        emailSender,
                        new DateProvider() {
                            @Override
                            public LocalDateTime getNowDateTime() {
                                return dateTime;
                            }
                        });
    }

    @Test
    void testSaveConfirmationToken() {
        when(confirmationTokenRepository.save(any(ConfirmationTokenEntity.class))).thenReturn(expectedConfirmationTokenEntity);

        confirmationTokenService.saveConfirmationToken(expectedConfirmationTokenEntity);

        verify(confirmationTokenRepository).save(expectedConfirmationTokenEntity);
    }

    @Test
    void getConfirmationTokenByToken() {
        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(Optional.of(expectedConfirmationTokenEntity));

        Optional<ConfirmationTokenEntity> confirmationTokenByToken = confirmationTokenService.getConfirmationTokenByToken(token);

        assertNotNull(confirmationTokenByToken);
        assertTrue(confirmationTokenByToken.isPresent());
        assertEquals(expectedConfirmationTokenEntity, confirmationTokenByToken.get());
    }

    @Test
    void setConfirmedAt() {
        when(confirmationTokenRepository.updateConfirmedAt(anyString(), any(LocalDateTime.class))).thenReturn(1);
        int rowsAffected = confirmationTokenService.setConfirmedAt(token);
        assertEquals(1, rowsAffected);
        verify(confirmationTokenRepository).updateConfirmedAt(token, dateTime);
    }

    @Test
    void createAndSendEmailConfirmationTokenEmailToUser() {
        String mockedToken = "6abd9db3-40b4-4c4c-bd7d-dece44f9f86e";

        ConfirmationTokenService confirmationTokenServiceStub =
                new ConfirmationTokenService(
                        confirmationTokenRepository,
                        emailSender,
                        new DateProvider() {
                            @Override
                            public LocalDateTime getNowDateTime() {
                                return dateTime;
                            }
                        }) {
                    @Override
                    String createToken(UserEntity userEntity) {
                        return mockedToken;
                    }
                };

        when(emailSender.send(any(MailDetail.class),any(EmailDataModel.class))).thenReturn(CompletableFuture.completedFuture(null));
        ConfirmationEmailData confirmationLink = ConfirmationEmailData.builder()
                .name(userEntity.getName())
                .link("http://localhost:8080/v1/registration/confirm?token=" + mockedToken).build();

        MailDetail mailDetail = MailDetail.newBuilder()
                .from("shareit")
                .to(userEntity.getEmail())
                .name(userEntity.getName())
                .subject("Confirm your email")
                .build();

        confirmationTokenServiceStub.createAndSendEmailConfirmationTokenEmailToUser(userEntity);
        verify(emailSender).send(mailDetail, confirmationLink);
    }

    @Test
    void createAndSendEmailConfirmationTokenEmailToUserThrowsEmailSenderException() {
        doThrow(new EmailSenderException("failed to send email")).when(emailSender).send(any(MailDetail.class), any(EmailDataModel.class));

        EmailSenderException emailSenderException = assertThrows(EmailSenderException.class, () -> confirmationTokenService.createAndSendEmailConfirmationTokenEmailToUser(userEntity));

        assertNotNull(emailSenderException);
        assertEquals("failed to send email", emailSenderException.getMessage());
    }
}