package com.shareit.service.registration;

import com.shareit.domain.dto.registration.UserCreated;
import com.shareit.domain.dto.registration.UserRegistration;
import com.shareit.domain.dto.registration.ConfirmationTokenEntity;
import com.shareit.domain.entity.UserEntity;
import com.shareit.domain.entity.UserState;
import com.shareit.service.UserService;
import com.shareit.utils.commons.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;
    private final RegistrationService registrationService;

    private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJlbWFpbCI6ImF5bGFuQGJvc2Nhcmluby5" +
            "jb20iLCJwYXNzd29yZCI6InlhMGdzcWh5NHd6dnV2YjQifQ.yN_8-" +
            "Mge9mFgsnYHnPEh_ZzNP7YKvSbQ3Alug9HMCsM";
    private final LocalDate birthDate = LocalDate.now();
    private final UserRegistration userRegistration = new UserRegistration(
            "any_email@mail.com",
            "any_password",
            "any_password",
            "any_name",
            birthDate);

    private UserEntity userEntity = new UserEntity(1L,
            "any_email@mail.com",
            "HHV$%%^5478yhgvbtFv34#$b",
            "any_name",
            birthDate,
            UserState.CONFIRMED);

    private ConfirmationTokenEntity expectedConfirmationTokenEntity = new ConfirmationTokenEntity(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15L),
            userEntity);

    private UserCreated expectedUserCreated = new UserCreated(1L);

    public RegistrationServiceTest() {
        this.confirmationTokenService = Mockito.mock(ConfirmationTokenService.class);
        this.userService = Mockito.mock(UserService.class);
        this.registrationService = new RegistrationService(confirmationTokenService, userService);
    }

    @Test
    public void testRegisterUser() {
        when(userService.signUpUser(any(UserRegistration.class))).thenReturn(userEntity);

        UserCreated userCreated = registrationService.registerUser(userRegistration);
        assertNotNull(userCreated);
        assertEquals(expectedUserCreated.getId(), userCreated.getId());
        verify(confirmationTokenService).createAndSendEmailConfirmationTokenEmailToUser(userEntity);

    }

    @Test
    public void testConfirmToken() {
        when(confirmationTokenService.getConfirmationTokenByToken(anyString()))
                .thenReturn(Optional.of(expectedConfirmationTokenEntity));

        String serviceReturn = registrationService.confirmToken(token);

        verify(confirmationTokenService).setConfirmedAt(token);
        verify(userService).enableAppUser(expectedConfirmationTokenEntity.getUser().getEmail());
        assertEquals("confirmed", serviceReturn);
    }

    @Test
    public void testConfirmTokenThrowBadRequestExceptionTokenNotFound() {
        when(confirmationTokenService.getConfirmationTokenByToken(anyString()))
                .thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> registrationService.confirmToken(token));

        assertNotNull(badRequestException);
        assertEquals("token not found", badRequestException.getMessage());
    }

    @Test
    public void testConfirmTokenThrowBadRequestExceptionEmailAlreadyConfirmed() {
        expectedConfirmationTokenEntity.setConfirmedAt(LocalDateTime.now().plusMinutes(3));
        when(confirmationTokenService.getConfirmationTokenByToken(anyString()))
                .thenReturn(Optional.of(expectedConfirmationTokenEntity));

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> registrationService.confirmToken(token));

        assertNotNull(badRequestException);
        assertEquals("email already confirmed", badRequestException.getMessage());
    }
}