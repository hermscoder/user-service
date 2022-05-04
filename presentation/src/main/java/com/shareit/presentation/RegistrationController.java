package com.shareit.presentation;

import com.shareit.domain.dto.registration.UserRegistration;
import com.shareit.domain.dto.registration.UserCreated;
import com.shareit.service.registration.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/v1/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<UserCreated> userRegistration(@Valid @RequestBody UserRegistration userRegistration) {
        return ResponseEntity.ok(registrationService.registerUser(userRegistration));
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return ResponseEntity.ok(registrationService.confirmToken(token));
    }
}
