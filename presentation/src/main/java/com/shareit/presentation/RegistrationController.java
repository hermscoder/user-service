package com.shareit.presentation;

import com.shareit.domain.dto.CreateUser;
import com.shareit.domain.dto.UserCreated;
import com.shareit.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/v1/registration")
public class RegistrationController {

    //TODO Add tests
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<UserCreated> userRegistration(@Valid @RequestBody CreateUser createUser) {
        return ResponseEntity.ok(registrationService.registerUser(createUser));
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return ResponseEntity.ok(registrationService.confirmToken(token));
    }
}
