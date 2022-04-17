package com.shareit.presentation;

import com.shareit.domain.dto.UserRegistration;
import com.shareit.domain.dto.UserCreated;
import com.shareit.service.registration.RegistrationService;
import com.shareit.utils.commons.exception.BadRequestException;
import com.shareit.utils.commons.exception.InvalidParameterException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RegistrationControllerTest {
    private final RegistrationController registrationController;
    private final RegistrationService registrationService;

    RegistrationControllerTest() {
        this.registrationService = Mockito.mock(RegistrationService.class);
        this.registrationController = new RegistrationController(registrationService);
    }

    @Test
    public void testUserRegister() {
        when(registrationService.registerUser(any(UserRegistration.class))).thenReturn(new UserCreated(1L, UUID.randomUUID().toString()));

        ResponseEntity<UserCreated> userCreatedResponseEntity = registrationController.userRegistration(
                new UserRegistration("any_email@mail.com",
                        "any_password",
                        "any_password",
                        "any_name",
                        LocalDate.now()));
        assertNotNull(userCreatedResponseEntity);
        assertEquals(HttpStatus.OK, userCreatedResponseEntity.getStatusCode());
        assertEquals(1L, userCreatedResponseEntity.getBody().getId());
    }

    @Test
    public void testUserRegisterThrowInvalidParameterException() {
        when(registrationService.registerUser(any(UserRegistration.class))).thenThrow(new InvalidParameterException("passwordConfirmation"));

        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> registrationController.userRegistration(
                new UserRegistration("any_email@mail.com",
                        "any_password",
                        "wrong_password",
                        "any_name",
                        LocalDate.now())));

        assertNotNull(invalidParameterException);
        assertEquals("passwordConfirmation", invalidParameterException.getParamName());
        assertEquals("Invalid param: passwordConfirmation", invalidParameterException.getMessage());
    }

    @Test
    public void testTokenConfirmation() {
        when(registrationService.confirmToken(anyString())).thenReturn("confirmed");

        ResponseEntity<String> confirm = registrationController.confirm("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJlbWFpbCI6ImF5bGFuQGJvc2Nhcmluby5" +
                "jb20iLCJwYXNzd29yZCI6InlhMGdzcWh5NHd6dnV2YjQifQ.yN_8-" +
                "Mge9mFgsnYHnPEh_ZzNP7YKvSbQ3Alug9HMCsM");

        assertNotNull(confirm);
        assertNotNull(confirm.getBody());
        assertEquals(HttpStatus.OK, confirm.getStatusCode());
        assertEquals("confirmed", confirm.getBody());
    }

    @Test
    public void testTokenConfirmationThrowBadRequestException() {
        when(registrationService.confirmToken(anyString())).thenThrow(new BadRequestException("token not found"));

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> registrationController.confirm(""));

        assertNotNull(badRequestException);
        assertEquals("token not found", badRequestException.getMessage());
    }


}