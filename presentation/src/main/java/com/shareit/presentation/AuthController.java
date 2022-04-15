package com.shareit.presentation;

import com.shareit.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/registration")
public class AuthController {

    //TODO Add tests
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return authService.confirmToken(token);
    }
}
